package ashleybaker.freddit_v1.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "feed", strict = false)
public class Feed implements Serializable {

    @Element(name = "icon")
    private String icon;

    @Element(name = "id")
    private String id;

    @Element(name = "logo")
    private String logo;

    @Element(name = "title")
    private String title;

    @Element(name = "updated")
    private String updated;

    @Element(name = "subtitle")
    private String subtitle;

    @Element(nameList = "entry")
    private List<Entry> entries;
}
