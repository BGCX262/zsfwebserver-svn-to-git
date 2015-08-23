package com.server.cache;

import com.server.identity.Identityer;

public class Right {
	private enum Priorty { MASTER, FRIEND }
	private Priorty priorty;
	private long identityCode;
	public boolean setMasterPriorty() {
		if(this.priorty == null){
			this.priorty = Priorty.MASTER;
			return true;
		}else{
			return false;
		}
	}
	public void setFriendPriorty() {
		if(this.priorty == null){
			this.priorty = Priorty.FRIEND;
		}
	}
	public long getIdentityCode() {
		return identityCode;
	}
	public void setIdentityCode(long identityCode) {
		this.identityCode = identityCode;
	}
	public boolean isMaster(){
		if(priorty != null && priorty.equals(Priorty.MASTER)){
			return true;
		}else{
			return false;
		}
	}
	public boolean isFriend(){
		if(priorty.equals(Priorty.FRIEND)){
			return true;
		}else{
			return false;
		}
	}
	public boolean canWrite(){
		if(this.priorty == Priorty.MASTER){
			return true;
		}else{
			return false;
		}
	}
	public boolean canRead(){
		if(this.priorty == Priorty.FRIEND || this.priorty == Priorty.MASTER){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean canDo(long id) {
		if (Identityer.identity(id, this.identityCode)) {
			if (isMaster()) {
				return true;
			}
		}
		return false;
	}
}
