package net.mynym.iam;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PrincipalRepoWithMap implements PrincipalRepo {
	public Map<Integer, Principal> repository = new HashMap<>();

	public void put(Principal toPut) {
		repository.put(toPut.id, toPut);
	}

	public Principal get(Integer toGet) {
		return repository.get(toGet);
	}

	public void remove(Principal toRemove) {
		repository.remove(toRemove.id);
	}
	
	public Set<Integer> keySet() {
		return repository.keySet();
	}

	@Override
	public void setSearcher(Searcher s) {
		// TODO Auto-generated method stub
		
	}

	@JsonIgnore
	@Override
	public Searcher getSearcher() {
		// TODO Auto-generated method stub
		return null;
	}
}
