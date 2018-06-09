package net.mynym.iam;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class Permission {
	EnumSet<Operation> operations;
	Principal principal; // One User or Group
	
	public Boolean checkAccess(User uToCheck, Operation oToCheck) {
		if (!operations.contains(oToCheck)) return false;
		if (principal instanceof User) {
			if (uToCheck == principal) return true;
			return false;
		}
		Set<Group> gSet = new HashSet<>();
		uToCheck.makeTokenSet(gSet);
		return gSet.contains(principal);
	}
	
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("Permit " + operations.toString() + " to " + principal.getName() + "\n");
		return b.toString();
	}

}
