package com.github.hualuomoli.gateway.client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.github.hualuomoli.gateway.api.entity.Request;
import com.github.hualuomoli.gateway.api.entity.Response;
import com.github.hualuomoli.gateway.api.enums.CodeEnum;
import com.github.hualuomoli.gateway.api.enums.EncryptionEnum;
import com.github.hualuomoli.gateway.api.enums.SignatureEnum;
import com.github.hualuomoli.gateway.api.lang.BusinessException;
import com.github.hualuomoli.gateway.api.lang.InvalidDataException;
import com.github.hualuomoli.gateway.api.parser.JSONParser;
import com.github.hualuomoli.gateway.client.dealer.EncryptionDealer;
import com.github.hualuomoli.gateway.client.dealer.SignatureDealer;
import com.github.hualuomoli.gateway.client.interceptor.Interceptor;
import com.github.hualuomoli.gateway.client.invoker.ClientInvoker;
import com.github.hualuomoli.gateway.client.lang.ClientException;

public class GatewayClient {

    private String version;
    private String partnerId;
    private SignatureEnum signature;
    private EncryptionEnum encryption;

    private List<Interceptor> interceptors;
    private ClientInvoker invoker;
    protected JSONParser jsonParser;

    public void setVersion(String version) {
        this.version = version;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public void setSignature(SignatureEnum signature) {
        this.signature = signature;
    }

    public void setEncryption(EncryptionEnum encryption) {
        this.encryption = encryption;
    }

    public void setInterceptors(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public void setInvoker(ClientInvoker invoker) {
        this.invoker = invoker;
    }

    public void setJsonParser(JSONParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    public void setEncryptionDealers(List<EncryptionDealer> encryptionDealers) {
        DealerUtils.setEncryptionDealers(encryptionDealers);
    }

    public void setSignatureDealers(List<SignatureDealer> signatureDealers) {
        DealerUtils.setSignatureDealers(signatureDealers);
    }

    /**
     * {@linkplain ClientInvoker#call(String, String)}
     */
    public String call(String method, String bizContent) throws BusinessException, ClientException {
        Request request = new Request();
        request.setVersion(version);
        request.setPartnerId(partnerId);
        request.setMethod(method);
        request.setTimestamp(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()));
        request.setBizContent(bizContent);
        request.setSignType(signature.name());
        request.setEncryptType(encryption == null ? null : encryption.name());

        try {

            // 前置拦截
            for (int i = 0, size = interceptors.size(); i < size; i++) {
                interceptors.get(i).preHandle(request);
            }

            // 执行业务处理
            String result = invoker.call(request);
            Response response = jsonParser.parseObject(result, Response.class);

            // 网关处理错误
            if (response.getCode() != CodeEnum.SUCCESS) {
                throw new ClientException(response.getCode(), response.getMessage());
            }

            // 后置拦截
            for (int size = interceptors.size(), i = size - 1; i >= 0; i--) {
                interceptors.get(i).postHandle(request, response);
            }

            return response.getResult();
        } catch (ClientException e) {
            throw e;
        } catch (BusinessException e) {
            throw e;
        } catch (InvalidDataException e) {
            throw new ClientException(CodeEnum.INVALID_DATA, e);
        } catch (Exception e) {
            throw new ClientException(CodeEnum.ERROR, e);
        }
        // end
    }

}
