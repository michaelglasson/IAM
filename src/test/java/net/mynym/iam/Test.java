package net.mynym.iam;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		PrincipalFactory factory = new PrincipalFactory("test");
		Group Ciao = factory.makeGroup("Ciao");
		Group Goodbye = factory.makeGroup("Goodbye");
		Group Hello = factory.makeGroup("Hello");
		Group Bonjour = factory.makeGroup("Bonjour");
		Group Zdravo = factory.makeGroup("Zdravo");
		User Mike = factory.makeUser("Mike");
		
		Zdravo.addMember(Mike);
		Ciao.addMember(Goodbye);
		Ciao.addMember(Hello);
		Goodbye.addMember(Bonjour);
		Bonjour.addMember(Zdravo);

		System.out.println(Zdravo.toString());
		StringBuilder buf = new StringBuilder();
		Zdravo.makeToken(buf);
		System.out.print(Zdravo.getName() + " is in Groups as follows: ");
		System.out.println(buf);
		buf.setLength(0);
		Bonjour.makeToken(buf);
		System.out.print(Bonjour.getName() + " is in Groups as follows: ");
		System.out.println(buf);
		buf.setLength(0);
		Ciao.makeFlatMemberListAsString(buf);
		System.out.println(buf);
		
		//XmlMapper mapper = new XmlMapper();
		
		ObjectMapper mapper = new ObjectMapper();
		
		FileWriter out = new FileWriter("repo.json");
		mapper.writer().withDefaultPrettyPrinter().writeValue(out, factory.repo);
		out.close();
		
		factory.repo = null;
				
		factory.repo = mapper.readValue(new FileReader("repo.json"), PrincipalRepoWithMap.class);
		
		//factory.repo.keySet().stream().forEach(k -> System.out.println(factory.repo.get(k)));
		
		out = new FileWriter("repo2.json");
		mapper.writer().withDefaultPrettyPrinter().writeValue(out, factory.repo);
		
		//PrincipalRepoLMDB r = new PrincipalRepoLMDB();
		//r.tutorial1();
		
	}
}
