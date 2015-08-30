package org.undp_iwomen.iwomen.data;

/**
 * Created by khinsandar on 7/29/15.
 */
public class ResourceItem {

    private String resourceText;
    private String resourceName;
    private String resourceImgPath;
    private int resourceImg;


    public ResourceItem(){

    }
    public ResourceItem(String resourceName ,String text ,  int i){
        super();
        this.resourceText = text;
        this.resourceImg = i;
        this.resourceName = resourceName;

    }

    public String getResourceText() {
        return resourceText;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public void setResourceText(String resourceText) {
        this.resourceText = resourceText;
    }

    public String getResourceImgPath() {
        return resourceImgPath;
    }

    public void setResourceImgPath(String resourceImgPath) {
        this.resourceImgPath = resourceImgPath;
    }

    public int getResourceImg() {
        return resourceImg;
    }

    public void setResourceImg(int resourceImg) {
        this.resourceImg = resourceImg;
    }
}
