package org.linuxmce.dce;

import java.util.*;

/**
 * A DCE Java SE Message.
 */
public class StandardMessage implements Message {

    private int deviceIDFrom = 0;
    private int deviceIDTo = 0;

    private Priority priority = Priority.NORMAL;
    private Type type = Type.COMMAND;

    private DCE.Command command;

    private int groupID = 0;
    private int categoryID = 0;
    private int templateID = 0;
    private boolean includeChildren = false;
    private BroadcastLevel broadcastLevel = BroadcastLevel.SAME_HOUSE;
    private Retry retry = Retry.NONE;
    private boolean relativeToSender = true;
    private ExpectedResponse expectedResponse = ExpectedResponse.NONE;
    private String devicesList = new String();

    private Map<DCE.CommandParam, String> parameters = new TreeMap<DCE.CommandParam, String>();


    private Map<DCE.CommandParam, byte[]> dataParameters = new TreeMap<DCE.CommandParam, byte[]>();


    private List<StandardMessage> extraMessages = new ArrayList<StandardMessage>();

    public List<StandardMessage> getExtraMessages() {
        return extraMessages;
    }

    public Map<DCE.CommandParam, String> getParameters() {
        return parameters;
    }

    public Map<DCE.CommandParam, byte[]> getDataParameters() {
        return dataParameters;
    }

    /**
     * TODO, DOCUMENT
     *
     * @return
     */
    public int getDeviceIDFrom() {
        return deviceIDFrom;
    }

    public void setDeviceIDFrom(int deviceIDFrom) {
        this.deviceIDFrom = deviceIDFrom;
    }

    /**
     * TODO, DOCUMENT
     *
     * @return
     */
    public int getDeviceIDTo() {
        return deviceIDTo;
    }

    public void setDeviceIDTo(int deviceIDTo) {
        this.deviceIDTo = deviceIDTo;
    }

    /**
     * TODO, DOCUMENT
     *
     * @return
     */
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * TODO, DOCUMENT
     *
     * @return
     */
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    /**
     * TODO, DOCUMENT
     *
     * @return
     */
    public int getId() {
        return command.getValue();
    }

    public DCE.Command getCommand() {
        return command;
    }

    public void setId(int id) {
        DCE.Command cmd = DCE.Command.fromValue(id);

        if (cmd == null) {
            throw new IllegalArgumentException("Command ID " + id + " does not exist");
        }

        this.command = cmd;
    }

    public void setCommand(DCE.Command command) {
        this.command = command;
    }

    /**
     * TODO, DOCUMENT
     *
     * @return
     */
    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    /**
     * TODO, DOCUMENT
     *
     * @return
     */
    public int getTemplateID() {
        return templateID;
    }

    public void setTemplateID(int templateID) {
        this.templateID = templateID;
    }

    @Override
    public String toString() {
        return "StandardMessage{" +
                "deviceIDFrom=" + deviceIDFrom +
                ", deviceIDTo=" + deviceIDTo +
                ", priority=" + priority +
                ", type=" + type +
                ", command=" + command +
                ", groupID=" + groupID +
                ", categoryID=" + categoryID +
                ", templateID=" + templateID +
                ", includeChildren=" + includeChildren +
                ", broadcastLevel=" + broadcastLevel +
                ", retry=" + retry +
                ", relativeToSender=" + relativeToSender +
                ", expectedResponse=" + expectedResponse +
                ", devicesList='" + devicesList + '\'' +
                ", parameters=" + parameters +
                ", dataParameters=" + dataParameters +
                ", embeddedMessages=" + extraMessages +
                '}';
    }

    /**
     * TODO, DOCUMENT
     *
     * @return
     */
    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    /**
     * TODO, DOCUMENT
     *
     * @return
     */
    public boolean isIncludeChildren() {
        return includeChildren;
    }

    public void setIncludeChildren(boolean includeChildren) {
        this.includeChildren = includeChildren;
    }

    /**
     * TODO, DOCUMENT
     *
     * @return
     */
    public BroadcastLevel getBroadcastLevel() {
        return broadcastLevel;
    }

    public void setBroadcastLevel(BroadcastLevel broadcastLevel) {
        this.broadcastLevel = broadcastLevel;
    }

    /**
     * TODO, DOCUMENT
     *
     * @return
     */
    public Retry getRetry() {
        return retry;
    }

    public void setRetry(Retry retry) {
        this.retry = retry;
    }

    /**
     * TODO, DOCUMENT
     *
     * @return
     */
    public boolean isRelativeToSender() {
        return relativeToSender;
    }

    public void setRelativeToSender(boolean relativeToSender) {
        this.relativeToSender = relativeToSender;
    }

    /**
     * TODO, DOCUMENT
     *
     * @return
     */
    public ExpectedResponse getExpectedResponse() {
        return expectedResponse;
    }

    public void setExpectedResponse(ExpectedResponse expectedResponse) {
        this.expectedResponse = expectedResponse;
    }

    /**
     * TODO, DOCUMENT
     *
     * @return
     */
    public String getDevicesList() {
        return devicesList;
    }

    public void setDevicesList(String devicesList) {
        this.devicesList = devicesList;
    }

    /**
     * Implement the Message serialisation contract
     ***/

    @Override
    public int getMessagePriority() {
        return priority.getValue();
    }

    @Override
    public int getMessageType() {
        return type.getValue();
    }

    @Override
    public int getMessageBroadcastLevel() {
        return broadcastLevel.getValue();
    }

    @Override
    public int getMessageRetry() {
        return retry.getValue();
    }

    @Override
    public int getMessageExpectedResponse() {
        return expectedResponse.getValue();
    }

    @Override
    public int getParameterCount() {
        return parameters.size();
    }

    @Override
    public String getParameterValue(int index) {
        return parameters.get(DCE.CommandParam.fromValue(index));
    }

    @Override
    public int getDataParameterCount() {
        return dataParameters.size();
    }

    @Override
    public byte[] getDataParameterValue(int index) {
        byte[] ret = dataParameters.get(index);
        if (ret == null) {
            return new byte[0];
        }
        return ret;
    }

    @Override
    public int getExtraMessageCount() {
        return getExtraMessages().size();
    }

    @Override
    public Message getExtraMessage(int index) {
        return getExtraMessages().get(index);
    }

    @Override
    public Enumeration getParameterIds() {
        return Collections.enumeration(parameters.keySet());
    }

    @Override
    public Enumeration getDataParameterIds() {
        return Collections.enumeration(dataParameters.keySet());
    }

    public static enum Priority {
        LOW(0),
        NORMAL(1),
        HIGH(2),
        URGENT(3);

        private int value;

        public int getValue() {
            return value;
        }

        Priority(int value) {
            this.value = value;
        }

        public static Priority fromValue(int i) {
            return values()[i];
        }
    }

    public static enum Type {
        COMMAND(1),
        EVENT(2),
        DATAPARM_CHANGE(3),
        REPLY(4),
        DATAPARM_REQUEST(5),
        LOG(6),
        SYSCOMMAND(7),
        REGISTER_INTERCEPTOR(8),
        MESSAGE_INTERCEPTED(9),
        EXEC_COMMAND_GROUP(10),
        START_PING(11),
        STOP_PING(12),
        PURGE_INTERCEPTORS(13),
        PENDING_TASKS(14);

        private int value;

        public int getValue() {
            return value;
        }

        Type(int value) {
            this.value = value;
        }

        public static Type fromValue(int i) {
            return values()[i];
        }
    }

    public static enum BroadcastLevel {
        NONE(0),
        DIRECT_SIBLINGS(1),
        SAME_COMPUTER(2),
        SAME_ROOM(3),
        SAME_HOUSE(4),
        ALL_HOUSES(5);

        private int value;

        public int getValue() {
            return value;
        }

        BroadcastLevel(int value) {
            this.value = value;
        }

        public static BroadcastLevel fromValue(int i) {
            return values()[i];
        }
    }

    public static enum Retry {
        NONE(0),
        RETRY(1),
        PERSIST(2);

        private int value;

        public int getValue() {
            return value;
        }

        Retry(int value) {
            this.value = value;
        }

        public static Retry fromValue(int i) {
            return values()[i];
        }
    }


    public static enum ExpectedResponse {
        NONE(0),
        REPLY_MESSAGE(1),
        REPLY_STRING(2),
        DELIVERY_CONFIRMATION(3);

        private int value;

        public int getValue() {
            return value;
        }

        ExpectedResponse(int value) {
            this.value = value;
        }

        public static ExpectedResponse fromValue(int i) {
            return values()[i];
        }
    }
}