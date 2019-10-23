/**
 * 
 */
/**
 * @author anasshatnawi
 *
 */
package fr.obeo.interview.task;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class ObeoTaskMain {

	public static void main(String[] args) {
		// print the welcome message
		System.out.println("Hello. This  progam aims to manipulate your EMF meta-model.");

		// create a resourceSet for our resources
		ResourceSet resourceSet = new ResourceSetImpl();

		// register EcoreResourceFactoryImpl as factory for .ecore files
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
				new EcoreResourceFactoryImpl());
		// load the ecore file using ResourceSet
		Resource metaModel = resourceSet.getResource(URI.createFileURI("./model/dart.ecore"), true);

		// register the package of the meta-model
		resourceSet.getPackageRegistry().put(((EPackage) metaModel.getContents().get(0)).getNsURI(),
				(EPackage) metaModel.getContents().get(0));

		// register XMIResourceFactoryImpl as factory for .dartspec files
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("dartspec",
				new XMIResourceFactoryImpl());
		// load the model file
		Resource model = resourceSet.getResource(URI.createFileURI("./model/dartlang.dartspec"), true);
		//call the run method that make the loop for user selection
		run(model, metaModel);
	}
	
	/**
	 * This method does a loop for taking the user input and close when the input is 0
	 * @param model
	 * @param metaModel
	 */
	public static void run(Resource model, Resource metaModel) {
		// to save the value that will be entered by the user, initial value should be
				// non-zero values to enter the loop
				int userInput = 1;
				// loop to take user input to perform tasks
				while (userInput != 0) {
					System.out.println("-Please press 1 to print the number of elements in your model.");
					System.out.println("-Please press 2 to print the labels of elements in your model.");
					System.out.println("-Please press 3 to add a new instance class to your model.");
					System.out.println("-Please press 4 to print the number of elements in your meta-model.");
					System.out.println("-Please press 5 to print the labels of elements in your meta-model.");
					System.out.println("-Please press 6 to add a new EClass to your meta-model.");
					System.out.println("-Please press 0 to exit the program.");
					// save the user choice
					try {
						userInput = new Scanner(System.in).nextInt();
				    } catch (InputMismatchException e){
				        // to catch if the user entered string, then we close the program
				    	userInput=0;
				    }
					// test the user choice
					switch (userInput) {
					case 0:
						// decided to exit the program
						System.out.println("Thanks for using this program. Hope to see you soon :) .");
						break;
					case 1:
						// decided to print the number of elements in the model
						printNumberOfElements(model);
						break;
					case 2:
						// decided to print labels of all elements in the model
						printAllLabels(model);
						break;
					case 3:
						// decided to add a new class in the model
						addNewClassToModel(model, metaModel);
						break;
					case 4:
						// decided to print the number of elements of the metamodel
						printNumberOfElements(metaModel);
						break;
					case 5:
						// decided to print labels of all elements of the metamodel
						printAllLabels(metaModel);
						break;
					case 6:
						// decided to add a new class to the metamodel
						addNewClassToMetaModel(metaModel);
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
	public static void addNewClassToMetaModel(Resource resource) {
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
	 * This method adds a new class to a given model based on its metamodel
	 * @param model
	 * @param metaModel
	 */
	public static void addNewClassToModel(Resource model, Resource metaModel) {
		// create a factory based on the metamodel package
		EFactory univInstance = ((EPackage) metaModel.getContents().get(0)).getEFactoryInstance();

		// read the name of the class from the user input
		System.out.println(
				"Please enter the name of the type of metamodel element (e.g., AngularType, HTML, Container...): ");
		String eClassName = new Scanner(System.in).nextLine();
		// assign the name to the class
		// add the class to the first package in the meta-model
		EClass eClass = getEClassByName(metaModel, eClassName);
		if (eClass == null) {
			System.out.println("Sorry. The EClass is not part of the metamodel.");
			return;
		}

		EObject newElement = univInstance.create(eClass);
		model.getContents().add(newElement);

		try {
			model.save(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("A new element of type " + eClassName + " has been added correctly to the model.");
	}

	/**
	 * This method
	 * 
	 * @param resource prints the number of elements in a given model or meta-model
	 */
	public static void printNumberOfElements(Resource resource) {
		// initial counter is 0
		int counter = 0;
		// get all elements in the meta-model
		TreeIterator<EObject> allElements = resource.getAllContents();
		// iterate over the tree to count only elements of the first-class-entity
		while (allElements.hasNext()) {
			allElements.next();
			counter++;
		}
		System.out.println("We identify " + counter + " elements in your meta-model.");
	}

	/**
	 * This method prints the labels of elements in a given model or meta-model
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
			} else {
				System.out.print("\t" + eObject.eClass().getName());
			}
			// to make a new line for every five elements
			counter++;
			if (counter %5 == 0) {
				System.out.println();
			}
		}
		System.out.println();
	}

	/**
	 * This method searchs for an EClass object in the metamodel based on a given
	 * name
	 * 
	 * @param metaModel
	 * @param eClassName
	 * @return
	 */
	public static EClass getEClassByName(Resource metaModel, String eClassName) {
		// get all elements in the meta-model
		TreeIterator<EObject> allElements = metaModel.getAllContents();
		// iterate over the tree, element by element
		while (allElements.hasNext()) {
			EObject eObject = (EObject) allElements.next();
			// check if it has a name(label) to print it
			if (eObject instanceof EClass) {
				EClass eClass = (EClass) eObject;
				if (eClass.getName().equalsIgnoreCase(eClassName)) {
					return eClass;
				}
			}
		}
		return null;
	}
}
