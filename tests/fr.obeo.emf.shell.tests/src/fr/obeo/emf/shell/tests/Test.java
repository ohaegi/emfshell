package fr.obeo.emf.shell.tests;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String prompt = "Library@7c87c960 >";

		System.out.println(prompt.replaceFirst("@[^\\s]* ", "@XX"));
	}

}
