package maru.core.model.net;

import java.io.Serializable;

import maru.core.model.IScenarioElement;

public class NetworkMessageWrapper implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final NetworkMessageID msg;
    private final IScenarioElement element;

    public NetworkMessageWrapper(NetworkMessageID msg, IScenarioElement element)
    {
        this.msg = msg;
        this.element = element;
    }

    public NetworkMessageID getMessageID()
    {
        return msg;
    }

    public IScenarioElement getElement()
    {
        return element;
    }

    @Override
    public String toString()
    {
        return "ID=" + msg + " Data=" + element.toString();
    }
}

//public class NetworkMessageWrapper implements Serializable
//{
//    private static final long serialVersionUID = 1L;
//
//////    public static final int ACTIVE_PROJECT_CHANGED  = 1001;
//////    public static final int ACTIVE_ELEMENT_CHANGED  = 1002;
////
//////    public static final int PROJECT_ADDED           = 2001;
//////    public static final int PROJECT_CHANGED         = 2002;
//////    public static final int PROJECT_REMOVED         = 2003;
////
////    public static final int SCENARIO_CREATED                            = 1001;
////    public static final int SCENARIO_ADDED                              = 1002;
////    public static final int SCENARIO_REMOVED                            = 1003;
////
////    public static final int ELEMENT_ADDED                               = 2001;
////    public static final int ELEMENT_REMOVED                             = 2002;
////    public static final int ELEMENT_RENAMED                             = 2003;
////    public static final int ELEMENT_COMMENTED                           = 2004;
////    public static final int ELEMENT_COLOR_CHANGED                       = 2005;
////    public static final int ELEMENT_IMAGE_CHANGED                       = 2006;
////
////    // todo: central body
////
////    public static final int GROUNDSTATION_INITIAL_COORDINATE_CHANGED    = 4001;
////    public static final int SPACECRAFT_INITIAL_COORDINATE_CHANGED       = 4002;
////
////    public static final int PROPAGATABLES_TIME_CHANGED                  = 5001;
////
////    // todo: timpoints
//
//    public int msg;
//    public List<Serializable> data;
//
//    public NetworkMessageWrapper(int msg, Serializable obj)
//    {
//        this.msg = msg;
//
//        this.data = new ArrayList<>();
//        this.data.add(obj);
//    }
//
//    public NetworkMessageWrapper(int msg, Serializable obj1, Serializable obj2)
//    {
//        this(msg, obj1);
//        data.add(obj2);
//    }
//
//    public NetworkMessageWrapper(int msg, Serializable obj1, Serializable obj2, Serializable obj3)
//    {
//        this(msg, obj1, obj2);
//        data.add(obj3);
//    }
//
//    @Override
//    public String toString()
//    {
//        // TODO Auto-generated method stub
////        return "MSG_ID=" + msg + " "
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("MSG_ID=");
//        sb.append(msg);
//
//        int i = 0;
//        for (Object obj : data) {
//            sb.append(" obj#");
//            sb.append(i++);
//            sb.append(obj.toString());
//        }
//
//        return sb.toString();
//    }
//}
