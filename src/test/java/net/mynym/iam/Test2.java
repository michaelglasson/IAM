package net.mynym.iam;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Test2 {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		PrincipalFactory factory = new PrincipalFactory("prod");
		Group Ciao = factory.makeGroup("Ciao");
		Group Goodbye = factory.makeGroup("Goodbye");
		Group Hello = factory.makeGroup("Hello");
		Group Bonjour = factory.makeGroup("Bonjour");
		Group Zdravo = factory.makeGroup("Zdravo");
		User Mike = factory.makeUser("Mike");
		
		Zdravo.addMember(Mike);
		Ciao.addMember(Mike);
		Ciao.addMember(Goodbye);
		Ciao.addMember(Hello);
		Goodbye.addMember(Bonjour);
		Bonjour.addMember(Zdravo);

	
		Principal p = factory.repo.get(3);
		System.out.println(p);
		/*
		FileWriter out = new FileWriter("repo.json");
		mapper.writer().withDefaultPrettyPrinter().writeValue(out, factory.repo);
		out.close();
		
		factory.repo = null;
				
		factory.repo = mapper.readValue(new FileReader("repo.json"), PrincipalRepoWithMap.class);
		
		factory.repo.keySet().stream().forEach(k -> System.out.println(factory.repo.get(k)));
		
		out = new FileWriter("repo2.json");
		mapper.writer().withDefaultPrettyPrinter().writeValue(out, factory.repo);
		
		PrincipalRepoLMDB r = new PrincipalRepoLMDB();
		//r.tutorial1();
		*/
	}
}
