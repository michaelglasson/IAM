package net.mynym.iam;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LMDBTest {
	static PrincipalFactory factory;
	static ObjectMapper mapper;
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

	static Group Ciao;
	static Group Goodbye;
	static Group Hello;
	static Group Bonjour;
	static Group Zdravo;
	static User Mike;

	@BeforeClass
	public static void setup() {
		factory = new PrincipalFactory("test");
		mapper = new ObjectMapper();

		Ciao = factory.makeGroup("Ciao");
		Goodbye = factory.makeGroup("Goodbye");
		Hello = factory.makeGroup("Hello");
		Bonjour = factory.makeGroup("Bonjour");
		Zdravo = factory.makeGroup("Zdravo");
		Mike = factory.makeUser("Mike");
		Zdravo.addMember(Mike);
		Ciao.addMember(Mike);
		Ciao.addMember(Goodbye);
		Ciao.addMember(Hello);
		Goodbye.addMember(Bonjour);
		Bonjour.addMember(Zdravo);
	}

	@Test
	public void test1() {
		assertTrue(Zdravo.memberIds.contains(Mike.id));
		assertTrue(Mike.memberOfIds.contains(Zdravo.id));
		assertTrue(Mike.getMemberOf().contains(Zdravo));
		assertTrue(Zdravo.getMember().contains(Mike));
	}
	
	@Test
	public void test2(){
		System.out.println("Testing repository retrieval by id (using ids 0-5)");
		for (Integer i = 0; i < 6; i++) {
			Principal p = factory.repo.get(i);
			assertTrue("Fetched principal id does not match fetch request", p.id == i);
			
		}
	}
	
	@Test
	public void test3() throws JsonGenerationException, JsonMappingException, IOException {
		File file = tempFolder.newFile("repo.json");
		FileWriter out = new FileWriter(file);
		mapper.writer().withDefaultPrettyPrinter().writeValue(out, factory.repo);
		out.close();
		PrincipalRepoWithMap testRepo = mapper.readValue(new FileReader(file), PrincipalRepoWithMap.class);
		System.out.println("Testing repository retrieval by id (using ids 0-5)");
		for (Integer i = 0; i < 6; i++) {
			Principal p = factory.repo.get(i);
			Principal q = testRepo.get(i);
			assertTrue("Fetched principal id does not match fetch request", p.id == q.id);
			
		}

		
	}
	
	
	
}
