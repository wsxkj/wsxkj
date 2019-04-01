package com.zpj.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

@SuppressWarnings("serial")
public class RadioTag extends TagSupport {

    private String type;
    private String no;
    private String name;
    private String style;
    private String onchange;
    private String onclick;
    private String cssclass;
    private String readonly;
    private String headerKey;
    private String headerValue;
    private String id;
    private String disabled;

    public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHeaderKey() {
        return headerKey;
    }

    public void setHeaderKey(String headerKey) {
        this.headerKey = headerKey;
    }

    public String getHeaderValue() {
        return headerValue;
    }

    public void setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getReadonly() {
        return readonly;
    }

    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    public String getCssclass() {
        return cssclass;
    }

    public void setCssclass(String cssclass) {
        this.cssclass = cssclass;
    }

    public String getOnchange() {
        return onchange;
    }

    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }

    public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    public int doStartTag() throws JspException {
        if (null == this.no) {
            this.no = "";
        }
        try {
            /* 插入取得当前SELECTBOX的值 */
            StringBuffer buffer = new StringBuffer();
            //StringBuffer option = new StringBuffer();
           /* if(this.headerValue != null && !this.headerValue.equals("")) {
                option.append("<option value='");
                if(this.headerValue != null && !this.headerValue.equals("")) {
                    option.append(this.headerKey);
                }
                option.append("'>" + this.headerValue + "</option>");
            }*/
            /* 插入option */
            Map<String,String> codeMap = new HashMap();//ResourceCodeUtil.getCodeMapByType(this.type);
            Set<Entry<String,String>> set = codeMap.entrySet();
            Iterator<Entry<String,String>> iterator = set.iterator();
            Entry<String,String> entry;
            while(iterator.hasNext()) {
                entry = iterator.next();
                buffer.append("<input type=\"radio\" value='" + entry.getKey() + "'");
                if(this.no.equals(entry.getKey())) {
                	buffer.append(" checked ");
                }
                /* 插入名称和ID */
                buffer.append(" name='" + this.name + "' ");
                //buffer.append(" id='" + this.id + "'");

                /* 插入样式 */
                if (null != this.style && !"".equals(this.style)) {
                    buffer.append(" style='" + this.style + "'");
                }
                if (null != this.readonly && !"".equals(this.readonly)) {
                    buffer.append(" readonly='" + this.readonly + "'");
                }
                if (null != this.disabled && !"".equals(this.disabled)) {
                    buffer.append(" disabled='" + this.disabled + "'");
                }
                if (null != this.onchange && !"".equals(onchange)) {
                    buffer.append(" onchange='" + this.onchange + "'");
                }
                if (null != this.onclick && !"".equals(onclick)) {
                    buffer.append(" onclick='" + this.onclick + "'");
                }
                if (null != this.cssclass && !"".equals(this.cssclass)) {
                    buffer.append(" class='" + this.cssclass + "'");
                }
                buffer.append("  >" + entry.getValue() + "");
            }
            
           
            /* 插入JSP画面 */
            this.pageContext.getOut().write(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_BODY_INCLUDE;
    }
}
