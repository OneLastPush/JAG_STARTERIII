package com.birikfr.mailbean;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FolderBean {
private StringProperty name; 
private IntegerProperty id;

public FolderBean(){
	this(-1,"");
	
}
public FolderBean(final int id, final String name){
	this.id = new SimpleIntegerProperty(id);
	this.name = new SimpleStringProperty(name);
}
public String getName() {
	return name.get();
}
public void setName(String name) {
	this.name = new SimpleStringProperty(name);
}
public int getId() {
	return id.get();
}
public void setId(int id) {
	this.id = new SimpleIntegerProperty(id);
}
public StringProperty getNameProperty(){
	return name;
}
public IntegerProperty getIdProperty(){
	return id;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + id.get();
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	FolderBean other = (FolderBean) obj;
	if (id.get() != other.id.get())
		return false;
	if (name == null) {
		if (other.name.get() != null)
			return false;
	} else if (!name.get().equals(other.name.get()))
		return false;
	return true;
}


}
