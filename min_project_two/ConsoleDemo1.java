package edu.cmu.sphinx.demo.min_project_two;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConsoleDemo1 {
	// create a script engine manager
	public static ScriptEngineManager manager = new ScriptEngineManager();
	// create a JavaScript engine
	public static ScriptEngine engine = manager.getEngineByName("js");

	public static DomParser domParser = new DomParser();
	public static Document mDoc = domParser.parseFile("dialog1.vxml");

	static BufferedReader br = new BufferedReader(new InputStreamReader(
			System.in));

	static String user_input;
	static String script;
	static boolean backTrack;
	static boolean retakeConfirmation;
	public static HashMap<String, String> variables = new HashMap<>();

	private static void promptMethod(Node node, String fieldNameAttribute)
			throws IOException {
		String[] splittedString;
		NodeList nodeChildren;

		if (fieldNameAttribute.contains("confirm")) {
			splittedString = fieldNameAttribute.split("_");
			nodeChildren = node.getChildNodes();
			nodeChildren.item(1).setTextContent(
					variables.get(splittedString[1]));
		}

		System.out.println("SYS: " + node.getTextContent());
		System.out.print("USR: ");
		user_input = br.readLine();
		variables.put(fieldNameAttribute, user_input);
	}

	public static void processBlockNode(Node node) throws NoSuchMethodException {
		String expression;
		ArrayList<String> function;
		Double result;
		NodeList children = node.getChildNodes();

		if (children.getLength() > 1) {
			expression = children.item(1).getAttributes().getNamedItem("expr")
					.getNodeValue();
			expression = expression.replace("(", " ").replace(")", "")
					.replace(",", " ");

			function = new ArrayList<String>(Arrays.asList(expression
					.split(" ")));
			result = evaluateFunction(function);
			children.item(1).setTextContent(Double.toString(result));
		}

		System.out.println("SYS: " + node.getTextContent());
	}

	public static ArrayList<String> getRuleItems(Node grammarNode) {

		NodeList grammarNodeChildren = grammarNode.getChildNodes();
		ArrayList<Node> ruleNodes = new ArrayList<>();

		for (int i = 0; i < grammarNodeChildren.getLength(); i++) {
			if (grammarNodeChildren.item(i).getNodeType() == Node.ELEMENT_NODE) {
				ruleNodes.add(grammarNodeChildren.item(i));
			}
		}

		Node ruleNode = ruleNodes.get(0);
		NodeList ruleNodeChildren = ruleNode.getChildNodes();
		ArrayList<Node> oneofNodes = new ArrayList<>();

		for (int j = 0; j < ruleNodeChildren.getLength(); j++) {
			if (ruleNodeChildren.item(j).getNodeType() == Node.ELEMENT_NODE) {
				oneofNodes.add(ruleNodeChildren.item(j));
			}
		}

		Node oneofNode = oneofNodes.get(0);
		NodeList itemNodes = oneofNode.getChildNodes();
		ArrayList<String> items = new ArrayList<>();

		String item;
		String[] splittedItem;
		for (int k = 0; k < itemNodes.getLength(); k++) {
			if (itemNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
				item = itemNodes.item(k).getTextContent();
				splittedItem = item.split(" ");
				items.add(splittedItem[0]);
			}
		}

		return items;
	}

	public static boolean checkUserInput(ArrayList<String> items,
			String fieldNameAttribute) {
		String userResponse = variables.get(fieldNameAttribute);
		if (items.contains(userResponse)) {
			return true;
		} else {
			return false;
		}
	}

	public static void evaluateIfConditions(Node ifNode) {
		String condAttribute = ifNode.getAttributes().getNamedItem("cond")
				.getNodeValue();
		// just to get rid of the '!' to be able to fetch it from the hashmap
		String condVariable = condAttribute.split("!")[1];

		Node clearNode = ifNode.getChildNodes().item(0);
		String nameListAttribute = clearNode.getAttributes()
				.getNamedItem("namelist").getNodeValue();
		String[] nameListVariables = nameListAttribute.split(" ");

		if ((variables.get(condVariable).equals("yes"))) {
			retakeConfirmation = false;
		} else {
			retakeConfirmation = true;
			//clear the variables
			for (int i = 0; i < nameListVariables.length; i++) {
				variables.put(nameListVariables[i], "");
			}
		}
	}

	public static void filledMethod(Node filledNode) {
		NodeList filledChildren = filledNode.getChildNodes();
		ArrayList<Node> ifNodes = new ArrayList<>();

		if (filledChildren.getLength() > 1) {
			for (int i = 0; i < filledChildren.getLength(); i++) {
				if (filledChildren.item(i).getNodeType() == Node.ELEMENT_NODE) {
					ifNodes.add(filledChildren.item(i));
				}
			}
			// to cover the case if there is more than one condition to evaluate
			for (int i = 0; i < ifNodes.size(); i++) {
				evaluateIfConditions(ifNodes.get(i));
			}
		} else {
			System.out.println("SYS: "
					+ filledChildren.item(0).getTextContent());
		}

	}

	public static void processFieldNode(Node node) throws IOException {

		// add the field name attribute to the hashmap of variables
		String fieldNameAttribute = node.getAttributes().getNamedItem("name")
				.getNodeValue();
		initializeVariable(fieldNameAttribute);

		ArrayList<String> items = new ArrayList<>();
		boolean correctUserInput;

		NodeList nodeChildren = node.getChildNodes();
		for (int i = 0; i < nodeChildren.getLength(); i++) {
			if (nodeChildren.item(i).getNodeType() == Node.ELEMENT_NODE) {
				// print
				// take input from user
				// store it in the correct place
				if (nodeChildren.item(i).getNodeName().equals("prompt")) {
					promptMethod(nodeChildren.item(i), fieldNameAttribute);
				}
				// check it against grammar
				if (nodeChildren.item(i).getNodeName().equals("grammar")) {
					items = getRuleItems(nodeChildren.item(i));
					correctUserInput = checkUserInput(items, fieldNameAttribute);
					if (!correctUserInput) {
						backTrack = true;
						break; // do not continue with the evaluation
					}
				}
				// evaluate the if condition
				if (nodeChildren.item(i).getNodeName().equals("filled")) {
					filledMethod(nodeChildren.item(i));
					if (retakeConfirmation) {
						break; // do not continue with the evaluation
					}
				}

			}
		}
	}

	public static void initializeVariable(String key) {
		variables.put(key, "");
	}

	public static void printHashMap(HashMap<String, String> hashmap) {
		for (String key : hashmap.keySet()) {
			String value = hashmap.get(key).toString();
			System.out.println(key + " : " + value);
		}
	}

	public static void displayWrongInputMsg() {
		System.out
				.println("SYS: Sorry! Your reply was not understood. I'll repeat the question.");
	}

	public static void displayFalseConfirmationMsg() {
		System.out.println("SYS: Okay! Lets take the information again");
	}

	public static Double evaluateFunction(ArrayList<String> function)
			throws NoSuchMethodException {
		Double result;
		String fnName = function.get(0);
		function.remove(0);

		String[] argumentValues = new String[function.size()];

		for (int i = 0; i < argumentValues.length; i++) {
			argumentValues[i] = variables.get(function.get(i));
		}
		try {
			engine.eval(script);
			Invocable invocableEngine = (Invocable) engine;
			result = (Double) invocableEngine.invokeFunction(fnName,
					argumentValues);
			return result;
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String extractScript(Node node) {
		return node.getTextContent().trim().replaceAll("\r\n", " ");
	}

	public static void main(String[] args) throws IOException,
			NoSuchMethodException {

		NodeList listOfScripts = mDoc.getElementsByTagName("script");
		Node scriptNode = listOfScripts.item(0);
		script = extractScript(scriptNode);

		NodeList ListOfForms = mDoc.getElementsByTagName("form");
		for (int i = 0; i < ListOfForms.getLength(); i++) {
			Node form = ListOfForms.item(i);
			NodeList formChildren = form.getChildNodes();

			for (int j = 0; j < formChildren.getLength(); ++j) {
				Node formNode = formChildren.item(j);
				if (formNode.getNodeType() == Node.ELEMENT_NODE) {
					if (formNode.getNodeName().equals("block")) {
						processBlockNode(formNode);
					} else if (formNode.getNodeName().equals("grammar")) {
						continue;
					} else if (formNode.getNodeName().equals("field")
							&& !backTrack && !retakeConfirmation) {
						processFieldNode(formNode);
						if (backTrack) {
							displayWrongInputMsg();
							j--;
							backTrack = false;
						}
						if (retakeConfirmation) {
							displayFalseConfirmationMsg();
							j -= 3;
							retakeConfirmation = false;
						}
					}

				}
			}//inner forloop (Form elements forloop)
			
		}//outer forloop (Forms forloop)
	} //main method
}