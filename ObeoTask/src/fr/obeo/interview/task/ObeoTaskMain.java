/**
 * 
 */
/**
 * @author anasshatnawi
 *
 */
package fr.obeo.interview.task;

import java.io.IOException;
import java.util.Scanner;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

public class ObeoTaskMain {

	public static void main(String[] args) {
		// print the welcome message
		System.out.println("Hello. This  progam aims to manipulate your EMF meta-model.");
		// create a resourceSet for our resources
		ResourceSet resourceSet = new ResourceSetImpl();
		// register EcoreResourceFactoryImpl as factory for ecore files
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
				new EcoreResourceFactoryImpl());
		// load the ecore file using ResourceSet
		Resource myMetaModel = resourceSet.getResource(
				URI.createFileURI("./model/dart.ecore"), true);
		// to save the value that will be entered by the user, initial value should ne
		// non-zero values to enter the loop
		int userInput = 1;
		// loop to take user input to perform tasks
		while (userInput != 0) {
			System.out.println("-Please press 1 to print the number of elements in your meta-model.");
			System.out.println("-Please press 2 to print the labels of elements in your meta-model.");
			System.out.println("-Please press 3 to add a new class to your meta-model.");
			System.out.println("-Please press 0 to exit the program.");
			// save the user choice
			userInput = new Scanner(System.in).nextInt();
			// test the user choice
			switch (userInput) {
			case 0:
				// decided to exit the program
				System.out.println("Thanks for using this program. Hope to see you soon :)");
				break;
			case 1:
				// decided to print the number of elements
				printNumberOfElements(myMetaModel);
				break;
			case 2:
				// decided to print labels of all elements
				printAllLabels(myMetaModel);
				break;
			case 3:
				// decided to add a new class
				addNewClasstoMetaModel(myMetaModel);

				break;
			default:
				System.out.println("Invalid number. Please select a number between 0 an 3");
				break;
			}
			System.out.println("_____________________________________________________________________________");
		}
	}

	/**
	 * This method adds a new class to the main package of a given meta-model
	 * 
	 * @param resource
	 */
	public static void addNewClasstoMetaModel(Resource resource) {
		// create a factory
		EcoreFactory theCoreFactory = EcoreFactory.eINSTANCE;
		// create a new class using the factory
		EClass newClass = theCoreFactory.createEClass();
		// read the name of the class from the user input
		System.out.println("Please enter the name of the new class.");
		String name = new Scanner(System.in).nextLine();
		// assign the name to the class
		newClass.setName(name);
		// add the class to the first package in the meta-model
		EPackage mainPackage = (EPackage) resource.getContents().get(0);
		mainPackage.getEClassifiers().add(newClass);
		try {
			resource.save(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("The " + newClass.getName() + " class has been added correctly.");		
	}

	/**
	 * This method
	 * 
	 * @param resource prints the number of elements in a given meta-model
	 */
	public static void printNumberOfElements(Resource resource) {
		// initial counter is 0
		int counter = 0;
		// get all elements in the meta-model
		TreeIterator<EObject> allElements = resource.getAllContents();
		// iterate over the tree to count only elements of the first-class-entity
		while (allElements.hasNext()) {
			EObject eObject = (EObject) allElements.next();
			if (eObject instanceof ENamedElement) {
				counter++;
			}
		}
		System.out.println("We identify " + counter + " elements in your meta-model.");
	}

	/**
	 * This method prints the labels of elements in a given meta-model
	 * 
	 * @param resource
	 */
	public static void printAllLabels(Resource resource) {
		System.out.println("The labels of the elements are as follows.");
		// get all elements in the meta-model
		TreeIterator<EObject> allElements = resource.getAllContents();
		int counter = 1;
		// iterate over the tree, element by element
		while (allElements.hasNext()) {
			EObject eObject = (EObject) allElements.next();
			// check if it has a name(label) to print it
			if (eObject instanceof ENamedElement) {
				ENamedElement namedElement = (ENamedElement) eObject;
				System.out.print("\t" + namedElement.getName() + "(" + namedElement.getClass().getSimpleName() + ")");
				// to make a new line for every five elements
				if (counter % 5 == 0) {
					System.out.println();
				}
				counter++;
			}
		}
		System.out.println();
	}
}
