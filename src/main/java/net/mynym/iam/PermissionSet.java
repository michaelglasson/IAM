package net.mynym.iam;

import java.util.HashSet;
import java.util.Set;

public class PermissionSet {
	String resourceId;
	Set<Permission> permissions = new HashSet<>();
	Boolean checkAccess(User uToCheck, Operation oToCheck) {
		for (Permission p: permissions) {
			if (p.checkAccess(uToCheck, oToCheck)) return true;
		}
		return false;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (Permission p : permissions) {
			s.append(p.toString());
		}
		return s.toString();
	}

}
