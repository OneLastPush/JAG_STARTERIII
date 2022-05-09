package com.birikfr.mailbean;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.slf4j.Logger;
/**
 * 
 * 
 * 
 * 
 * */
public class AttachBean {
	private ArrayList<Boolean> contentId;
	private ArrayList<String> name;
	private ArrayList<String> size;
	private ArrayList<byte[]> files;


	

	
	public AttachBean(){
		this.contentId = new ArrayList<>();
		this.name = new ArrayList<>();
		this.size = new ArrayList<>();
		this.files = new ArrayList<>();

	}
	/**
	 * 
	 * This add method with the file path retrieve all the information about the file 
	 * Note that the code that could throw the IOException is the first to be invoked to not assign 
	 * unnecessary field
	 * 
	 * @param filepath
	 * @param embed
	 * */
	public final void add(final String filepath, final boolean embed) throws IOException{
		
		Path path = Paths.get(filepath);
		
		this.files.add(Files.readAllBytes(path));
		File f = new File(filepath);
		this.name.add(f.getName());
		this.size.add(f.length() + "");
		this.contentId.add(embed);
	}
	public final void add(final String filepath, final boolean embed, Logger log) throws IOException{
		log.info("IN add");

		FileInputStream fileInputStream=null;
        
        File file = new File(filepath);
        
        byte[] bFile = new byte[(int) file.length()];
        
        
            //convert file into array of bytes
	    fileInputStream = new FileInputStream(file);
	    fileInputStream.read(bFile);
	    fileInputStream.close();
	    log.info("IM GOING TO ADD INTO FILES");
		this.files.add(bFile);
		log.info("Just added to byte arrayList a byte[]" + files.size());
		File f = new File(filepath);
		this.name.add(f.getName());
		this.size.add(f.length() + "");
		this.contentId.add(embed);
	}
	/**
	 * @param filepath
	 * 
	 * @return removed
	 * This remove method delete the file that the String that the user sending to is pointing
	 * 
	 * If the return value is false the file was not removed because it was not attached/embedded in first place
	 * *//**
	public final boolean removeByName(final String filepath) throws IOException{
		File f = new File(filepath);
		
		if(!f.exists()){
			throw new IOException("The file that you are looking for does not exist! : " + filepath);
		}
		boolean removed = false;
		
		int index = this.path.indexOf(filepath);
		
		if(index == -1){
			return removed;
		}else{
			this.path.remove(index);
			this.contentId.remove(index);
			this.name.remove(index);
			this.size.remove(index);
			this.files.remove(index);
			removed = true;
		}
		
		return removed;
	}*/
	/**
	 * @return number of files
	 * 
	*/
	public int getNumFiles() {
		return contentId.size();
	}
	/**
	 * @return contentId
	 * 
	*/
	public ArrayList<Boolean> getContentId() {
		return contentId;
	}
	/**
	 * @return name
	 * 
	*/
	public ArrayList<String> getName() {
		return name;
	}
	/**
	 * @return size
	 * 
	*/
	public ArrayList<String> getSize() {
		return size;
	}
	/**
	 * @return files
	 * 
	*/
	public ArrayList<byte[]> getFiles() {
		return files;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((files == null) ? 0 : files.hashCode());
		return result;
	}

	//@Override
	public boolean equals(Object obj, Logger log) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttachBean other = (AttachBean) obj;
		log.info("conversion done");
		if (files == null) {
			if (other.files != null)
			{
				return false;
				}
		} else {
			if((files.size() > 0 && other.files.size() > 0) && (files.size() == other.files.size()) )
			for(int index =  0, len = files.size(); index < len; index++){
				if ((!files.get(index).equals(other.files.get(index))))	
					return false;
		}
	}
			if (contentId == null) {
				if (other.contentId != null)
				{
					return false;
					}
			} else {
				if((contentId.size() > 0 && other.contentId.size() > 0) && (contentId.size() == other.contentId.size()) )
				for(int index =  0, len = contentId.size(); index < len; index++){
					if ((!contentId.get(index).equals(other.contentId.get(index))))	
						return false;
			}
		}
		log.info("Good job");
		return true;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttachBean other = (AttachBean) obj;
		
		if (files == null) {
			if (other.files != null)
			{
				return false;
				}
		} else {
			if((files.size() > 0 && other.files.size() > 0) && (files.size() == other.files.size()) )
			for(int index =  0, len = files.size(); index < len; index++){
				if ((!files.get(index).equals(other.files.get(index))))	
					return false;
		}
	}
			if (contentId == null) {
				if (other.contentId != null)
				{
					return false;
					}
			} else {
				if((contentId.size() > 0 && other.contentId.size() > 0) && (contentId.size() == other.contentId.size()) )
				for(int index =  0, len = contentId.size(); index < len; index++){
					if ((!contentId.get(index).equals(other.contentId.get(index))))	
						return false;
			}
		}

		return true;
	}
	
	
}
