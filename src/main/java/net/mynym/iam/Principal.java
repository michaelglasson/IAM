package net.mynym.iam;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public abstract class Principal {
	public Integer id;
	String name;
	public Set<Integer> memberOfIds = new LinkedHashSet<>();
	private Set<Group> memberOf = new LinkedHashSet<>();
	@JsonIgnore
	PrincipalRepo repo;

	public void put() {
		repo.put(this);
	}

	public void addAsMemberOf(Group toAddTo) {
		toAddTo.justAddMember(this);
		memberOf.add(toAddTo);
		memberOfIds.add(toAddTo.id);
		put();
		toAddTo.put();
	}

	public Document toLuceneDoc() {
		Document doc = new Document();
		doc.add(new StringField("id", id.toString(), Field.Store.YES));
		doc.add(new StringField("name", name, Field.Store.YES));
		getMemberOf().stream().forEach(m -> doc.add(new StringField("memberOf", m.name, Field.Store.YES)));
		return doc;
	}
	
	public String toString() {
		return "Id: " + name + ", Principal type: " + this.getClass().getSimpleName() + ", Count of memberOf: "
				+ memberOfIds.size();
	}
	
	public void makeToken(StringBuilder buf) {
		for (Group g : getMemberOf()) {
			buf.append("|" + g.getName());
			g.makeToken(buf);
		}
		if (buf.length() > 0 && buf.substring(0, 1).equalsIgnoreCase("|"))
			buf.deleteCharAt(0);
	}

	public void makeTokenSet(Set<Group> tSet) {
		for (Group g : getMemberOf()) {
			tSet.add(g);
			g.makeTokenSet(tSet);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonIgnore
	public Set<Group> getMemberOf() {
		if (memberOf.isEmpty()) {
			memberOfIds.stream().forEach(i -> memberOf.add((Group) repo.get(i)));
		}
		return memberOf;
	}


	public PrincipalRepo getRepo() {
		return repo;
	}

	public void setRepo(PrincipalRepo repo) {
		this.repo = repo;
	}
}
