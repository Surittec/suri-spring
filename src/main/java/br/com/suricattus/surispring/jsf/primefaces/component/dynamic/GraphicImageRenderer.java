package br.com.suricattus.surispring.jsf.primefaces.component.dynamic;

import java.util.UUID;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;

import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.model.StreamedContent;
import org.primefaces.util.Constants;

import br.com.suricattus.surispring.spring.util.ApplicationContextUtil;

/**
 * Renderer para corrigir o dynamic content com os escopos distintos
 * 
 * @author Lucas Lins
 *
 */
public class GraphicImageRenderer extends org.primefaces.component.graphicimage.GraphicImageRenderer{
	
	@Override
	protected String getImageSrc(FacesContext context, GraphicImage image) {
		String src = null;
		Object value = image.getValue();

		if(value == null || value.toString().trim().length() == 0) {
            src = "";
        }
        else {
            if(value instanceof StreamedContent) {
                StreamedContent streamedContent = (StreamedContent) value;
                Resource resource = context.getApplication().getResourceHandler().createResource("dynamiccontent", "primefaces", streamedContent.getContentType());
                String resourcePath = resource.getRequestPath();
                String rid = createUniqueContentId(context);
                StringBuilder builder = new StringBuilder(resourcePath);

                builder.append("&").append(Constants.DYNAMIC_CONTENT_PARAM).append("=").append(rid);

                for(UIComponent kid : image.getChildren()) {
                    if(kid instanceof UIParameter) {
                        UIParameter param = (UIParameter) kid;

                        builder.append("&").append(param.getName()).append("=").append(param.getValue());
                    }
                }

                src = builder.toString();
                
                context.getExternalContext().getSessionMap().put(rid, getExpression(context.getELContext(), image.getValueExpression("value"), rid));
            }
            else {
                src = getResourceURL(context, (String) value);
            }

            //Add caching if needed
            if(!image.isCache()) {
                src += src.contains("?") ? "&" : "?";

                src = src + "primefaces_image=" + UUID.randomUUID().toString();
            }
        }

		return src;
	}

	protected String getExpression(ELContext elContext, ValueExpression valueExpression, String rid){
		DynamicContentHolder dynamicContentHolder = ApplicationContextUtil.getContext().getBean(DynamicContentHolder.class);
        dynamicContentHolder.put(rid, (StreamedContent) valueExpression.getValue(elContext));
        return String.format(br.com.suricattus.surispring.jsf.util.Constants.DYNAMIC_CONTENT_EXPRESSION, rid);
	}
	
}
