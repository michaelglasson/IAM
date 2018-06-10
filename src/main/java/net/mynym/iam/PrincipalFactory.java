package net.mynym.iam;

public class PrincipalFactory {
	PrincipalRepo repo;
	Searcher s;
	Integer highestId = 0;

	public PrincipalFactory(String mode) {
		s = new Searcher();
		if (mode.equalsIgnoreCase("test")) {
			repo = new PrincipalRepoWithMap();
			repo.setSearcher(s);
		}
		if (mode.equalsIgnoreCase("prod")) {
			repo = new PrincipalRepoLMDB();
			repo.setSearcher(s);
		}
	}

	public User makeUser(String userName) {
		User u = new User();
		u.id = highestId++;
		u.setName(userName);
		u.setRepo(repo);
		u.put();
		return u;
	}

	public Group makeGroup(String groupName) {
		Group g = new Group();
		g.id = highestId++;
		g.setName(groupName);
		g.setRepo(repo);
		g.put();
		return g;
	}
}
