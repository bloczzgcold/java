package com.github.hualuomoli.gateway.server;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hualuomoli.gateway.api.entity.Request;
import com.github.hualuomoli.gateway.api.entity.Response;
import com.github.hualuomoli.gateway.api.enums.CodeEnum;
import com.github.hualuomoli.gateway.api.lang.BusinessException;
import com.github.hualuomoli.gateway.api.lang.InvalidDataException;
import com.github.hualuomoli.gateway.api.lang.NoAuthorityException;
import com.github.hualuomoli.gateway.api.lang.NoPartnerException;
import com.github.hualuomoli.gateway.api.lang.NoRouterException;
import com.github.hualuomoli.gateway.server.business.BusinessHandler;
import com.github.hualuomoli.gateway.server.business.interceptor.AuthorityInterceptor;
import com.github.hualuomoli.gateway.server.business.interceptor.BusinessInterceptor;
import com.github.hualuomoli.gateway.server.dealer.EncryptionDealer;
import com.github.hualuomoli.gateway.server.dealer.SignatureDealer;
import com.github.hualuomoli.gateway.server.interceptor.Interceptor;

/**
 * 网关服务器
 */
public class GatewayServer {

    private BusinessHandler businessHandler;
    private AuthorityInterceptor authorityInterceptor;
    private List<Interceptor> interceptors = new ArrayList<Interceptor>();
    private List<BusinessInterceptor> businessInterceptors = new ArrayList<BusinessInterceptor>();

    public void setBusinessHandler(BusinessHandler businessHandler) {
        this.businessHandler = businessHandler;
    }

    public void setAuthorityInterceptor(AuthorityInterceptor authorityInterceptor) {
        this.authorityInterceptor = authorityInterceptor;
    }

    public void setInterceptors(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public void setBusinessInterceptors(List<BusinessInterceptor> businessInterceptors) {
        this.businessInterceptors = businessInterceptors;
    }

    public Response execute(HttpServletRequest req, HttpServletResponse res) {
        Request request = this.parse(req);

        Response response = new Response();
        try {
            // 前置拦截
            for (int i = 0, size = interceptors.size(); i < size; i++) {
                interceptors.get(i).preHandle(req, res, request);
            }

            // 执行业务 
            String result = businessHandler.execute(req, res//
                    , request.getPartnerId(), request.getMethod(), request.getBizContent()//
                    , authorityInterceptor, businessInterceptors);

            // 设置返回信息
            response.setCode(CodeEnum.SUCCESS);
            response.setMessage("处理成功");
            response.setResult(result);

        } catch (InvalidDataException e) {
            response.setCode(CodeEnum.INVALID_DATA);
            response.setMessage(e.getMessage());
        } catch (NoPartnerException e) {
            response.setCode(CodeEnum.NO_PARTNER);
            response.setMessage(e.getMessage());
        } catch (NoAuthorityException e) {
            response.setCode(CodeEnum.NO_AUTHORITY);
            response.setMessage(e.getMessage());
        } catch (NoRouterException e) {
            response.setCode(CodeEnum.NO_ROUTER);
            response.setMessage(e.getMessage());
        } catch (BusinessException e) {
            response.setCode(CodeEnum.BUSINESS);
            response.setMessage("业务处理错误");
            response.setSubCode(e.getSubCode());
            response.setSubMessage(e.getSubMessage());
        } catch (Exception e) {
            response.setCode(CodeEnum.ERROR);
            response.setMessage(e.getMessage());
        } finally {
            // 后置拦截
            for (int size = interceptors.size(), i = size - 1; i >= 0; i--) {
                interceptors.get(i).postHandle(req, res, request, response);
            }
        }

        return response;
    }

    public void setEncryptionDealers(List<EncryptionDealer> encryptionDealers) {
        DealerUtils.setEncryptionDealers(encryptionDealers);
    }

    public void setSignatureDealers(List<SignatureDealer> signatureDealers) {
        DealerUtils.setSignatureDealers(signatureDealers);
    }

    /**
     * 解析HTTP请求
     * @param req HTTP请求
     * @return 请求信息
     */
    private Request parse(HttpServletRequest req) {
        Request request = new Request();
        request.setVersion(req.getParameter("version"));
        request.setPartnerId(req.getParameter("partnerId"));
        request.setMethod(req.getParameter("method"));
        request.setTimestamp(req.getParameter("timestamp"));
        request.setBizContent(req.getParameter("bizContent"));
        request.setSignType(req.getParameter("signType"));
        request.setSign(req.getParameter("sign"));
        request.setEncryptType(req.getParameter("encryptType"));
        return request;
    }

}
