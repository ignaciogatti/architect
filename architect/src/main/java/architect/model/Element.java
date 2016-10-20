package architect.model;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sun.xml.bind.AnyTypeAdapter;

@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface Element {

}
