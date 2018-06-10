package net.mynym.iam;

public interface PrincipalRepo {

	void put(Principal toAdd);

	Principal get(Integer toGet);

	void remove(Principal toRemove);
	
	void setSearcher(Searcher s);
	
	Searcher getSearcher();
}
