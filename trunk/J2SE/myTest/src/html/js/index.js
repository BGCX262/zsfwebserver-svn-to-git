window.onload = function() {
	var mStr = "可用逗号分隔，顺序为从前往后";
	var nStr = "请输入姓名";
	
	mForm.eatname.value = mStr;
	mForm.name.value = nStr;
	
	mForm.eatname.style.color = "gray";
	mForm.name.style.color = "gray";
	
	mForm.eatname.onfocus = function() {
		if (mForm.eatname.value == mStr) {
			mForm.eatname.value = "";
			mForm.eatname.style.color = "black";
		}
	}
	
	mForm.eatname.onblur = function() {
		var str = mForm.eatname.value;
		if (str.length <= 0) {
			mForm.eatname.style.color = "gray";
			mForm.eatname.value = mStr;
		}
	}
	
	mForm.name.onfocus = function() {
		if (mForm.name.value == nStr) {
			mForm.name.value = "";
			mForm.name.style.color = "black";
		}
	}
	
	mForm.name.onblur = function() {
		var str = mForm.name.value;
		if (str.length <= 0) {
			mForm.name.style.color = "gray";
			mForm.name.value = nStr;
		}
	}
	
	mForm.onsubmit = function() {
		var name = mForm.name.value;
		var eatname = mForm.eatname.value;
		if (eatname == mStr || name == nStr) {
			alert("请填写完整");
			return false;
		}
	}
	
}
