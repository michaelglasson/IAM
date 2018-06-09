package net.mynym.iam;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
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
    public TemporaryFolder _tempFolder = new TemporaryFolder();

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
		assertTrue(Mike.memberOfId.contains(Zdravo.id));
		assertTrue(Mike.getMemberOf().contains(Zdravo));
		assertTrue(Zdravo.getMember().contains(Mike));
	}
	
	@Test
	public void test2(){
		Principal p = factory.repo.get(3);
		assertTrue(p.id == 3);
	}
	
	@Test
	public void test3() throws JsonGenerationException, JsonMappingException, IOException {
		Principal p = Zdravo;
		FileWriter out = new FileWriter("repo.json");
		mapper.writer().withDefaultPrettyPrinter().writeValue(out, factory.repo);
		out.close();
		
		factory.repo = null;
		
		factory.repo = mapper.readValue(new FileReader("repo.json"), PrincipalRepoWithMap.class);	
		
	}
	
	@Test
	public void tmp() throws IOException {
		File tempFile = _tempFolder.newFile("file.txt");

		
	}
	
	 static TemporaryFolder _tempFolder2;

	    @After
	    public void after() {
	        _tempFolder2 = _tempFolder;
	        System.out.println(_tempFolder2.getRoot().exists());
	    }

	    @AfterClass
	    public static void afterClass() {
	        System.out.println(_tempFolder2.getRoot().exists());
	    }

	    @Test
	    public void pass() {
	    }
	
}
