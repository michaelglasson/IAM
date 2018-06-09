package net.mynym.iam;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Group extends Principal {
	private Set<Principal> member = new LinkedHashSet<>();
	public Set<Integer> memberIds = new LinkedHashSet<>();
	@JsonIgnore
	public Set<Principal> getMember() {
		if (member.isEmpty()) {
			memberIds.stream().forEach(i -> addMember(repo.get(i)));
		}
		return member;
	}

	public void justAddMember(Principal toAdd) {
		member.add(toAdd);
		memberIds.add(toAdd.id);
	}

	public void addMember(Principal toAdd) {
		toAdd.addAsMemberOf(this);
	}

	@Override
	public Document toLuceneDoc() {
		Document doc = super.toLuceneDoc();
		getMember().stream().forEach(m -> doc.add(new StringField("member", m.name, Field.Store.YES)));
		return doc;
	}

	public void makeFlatMemberListAsString(StringBuilder buf) {
		for (Principal p : member) {
			buf.append("|" + p.getName());
			if (p instanceof Group) {
				((Group) p).makeFlatMemberListAsString(buf);
			}
		}
		if (buf.length() > 0 && buf.substring(0, 1).equalsIgnoreCase("|"))
			buf.deleteCharAt(0);
	}
}
