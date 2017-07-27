<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link href="https://cdn.bootcss.com/font-awesome/4.6.0/css/font-awesome.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="css/index.css">
</head>

<body>
    <div class="container">
        <div class="header">
            <h3>${method}</h3>
            <span class="description">(${title})</span>
            <p>${description}</p>
        </div>
        <!-- content -->
        <div class="content">
            <!-- url -->
            <div class="data url">
                <h5>请求地址</h5>
                <table>
                    <tr>
                        <th width="35%">环境</th>
                        <th width="65%">HTTPS请求地址</th>
                    </tr>
                    <tr>
                        <td>正式环境</td>
                        <td>${serverUrl}</td>
                    </tr>
                </table>
            </div>
            <!-- ./url -->

            <!-- 公共请求参数 -->
            <div class="data common-request">
                <h5>公共请求参数</h5>
                <table>
                    <tr>
                        <th>参数</th>
                        <th>类型</th>
                        <th>是否必填</th>
                        <th>最大长度</th>
                        <th>描述</th>
                        <th>示例值</th>
                    </tr>
                    <tr>
                        <td>partnerId</td>
                        <td>String</td>
                        <td>是</td>
                        <td>32</td>
                        <td>合作伙伴ID</td>
                        <td>2014072300007148</td>
                    </tr>
                    <tr>
                        <td>method</td>
                        <td>String</td>
                        <td>是</td>
                        <td>128</td>
                        <td>接口名称</td>
                        <td>${method}</td>
                    </tr>
                    <tr>
                        <td>timestamp</td>
                        <td>String</td>
                        <td>是</td>
                        <td>19</td>
                        <td>发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"</td>
                        <td>2014-07-24 03:07:50</td>
                    </tr>
                    <tr>
                        <td>nonceStr</td>
                        <td>String</td>
                        <td>是</td>
                        <td>32</td>
                        <td>随机字符串</td>
                        <td>2jSuRZ0jQFf3Z5Em</td>
                    </tr>
                    <tr>
                        <td>signType</td>
                        <td>String</td>
                        <td>是</td>
                        <td>10</td>
                        <td>商户生成签名字符串所使用的签名算法类型，目前支持RSA</td>
                        <td>RSA</td>
                    </tr>
                    <tr>
                        <td>encryptType</td>
                        <td>String</td>
                        <td>否</td>
                        <td>10</td>
                        <td>商户加密请求业务内容所使用的加密算法类型，目前支持AES。注：加密要在签名后执行</td>
                        <td>AES</td>
                    </tr>
                    <tr>
                        <td>sign</td>
                        <td>String</td>
                        <td>是</td>
                        <td>256</td>
                        <td>商户请求参数的签名串，<a href="sign.html">详见签名</a></td>
                        <td>详见示例</td>
                    </tr>
                    <tr>
                        <td>version</td>
                        <td>String</td>
                        <td>是</td>
                        <td>6</td>
                        <td>调用的接口版本，固定为：1.0</td>
                        <td>1.0</td>
                    </tr>
                    <tr>
                        <td>bizContent</td>
                        <td>String</td>
                        <td>是</td>
                        <td>-</td>
                        <td>请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档</td>
                        <td></td>
                    </tr>
                </table>
            </div>
            <!-- ./公共请求参数 -->
            
            <!-- 请求参数 -->
            <div class="data request"></div>
                <h5>请求参数</h5>
                <table>
                    <tr>
                        <th>参数</th>
                        <th>类型</th>
                        <th>是否必填</th>
                        <th>最大长度</th>
                        <th>描述</th>
                        <th>示例值</th>
                    </tr>
                    <#list requestParameters as requestParameter>
                    <#if requestParameter.type == 'Array' || requestParameter.type == 'Object'>
                    <tr class="trigger">
                        <td>
                            <span>
                                <i class="fa fa-plus-square-o"></i>
                                <i class="fa fa-minus-square-o hide"></i>
                            </span>
                            ${requestParameter.name}
                        </td>
                        <td>${requestParameter.type!''}</td>
                        <td>${requestParameter.required?string('是','否')}</td>
                        <td>${requestParameter.maxLength!'-'}</td>
                        <td>${requestParameter.description}</td>
                        <td>${requestParameter.sample!''}</td>
                    </tr>
                    <tr class="trigger-data hide">
                        <td colspan="6">
                            <#list requestParameter.parameters as parameter>
                            <ul>
                                <li><span>${parameter.name}</span></li>
                                <li><span>${parameter.type}</span></li>
                                <li><span>${parameter.required?string('是','否')}</span></li>
                                <li><span>${parameter.maxLength!'-'}</span></li>
                                <li><span>${parameter.description}</span></li>
                                <li><span>${parameter.sample!''}</span></li>
                            </ul>
                            </#list>
                        </td>
                    </tr>

                    <#else>
                    <tr>
                        <td>${requestParameter.name}</td>
                        <td>${requestParameter.type!''}</td>
                        <td>${requestParameter.required?string('是','否')}</td>
                        <td>${requestParameter.maxLength!'-'}</td>
                        <td>${requestParameter.description}</td>
                        <td>${requestParameter.sample!''}</td>
                    </tr>
                    </#if>
                    </#list>
                </table>
            </div>
            <!-- ./请求参数 -->

            <!-- 公共响应参数 -->
            <div class="data common-response">
                <h5>公共响应参数：</h5>
                <table>
                    <tr>
                        <th>参数</th>
                        <th>类型</th>
                        <th>是否必填</th>
                        <th>最大长度</th>
                        <th>描述</th>
                        <th>示例值</th>
                    </tr>
                    <tr>
                        <td>code</td>
                        <td>String</td>
                        <td>是</td>
                        <td>-</td>
                        <td>网关返回码,详见文档</td>
                        <td>40004</td>
                    </tr>
                    <tr>
                        <td>message</td>
                        <td>String</td>
                        <td>是</td>
                        <td>-</td>
                        <td>网关返回码描述,详见文档</td>
                        <td>Business Failed</td>
                    </tr>
                    <tr>
                        <td>subCode</td>
                        <td>String</td>
                        <td>否</td>
                        <td>-</td>
                        <td>业务返回码,详见文档</td>
                        <td>ACQ.TRADE_HAS_SUCCESS</td>
                    </tr>
                    <tr>
                        <td>subMessage</td>
                        <td>String</td>
                        <td>否</td>
                        <td>-</td>
                        <td>业务返回码描述,详见文档</td>
                        <td>交易已被支付</td>
                    </tr>
                    <tr>
                        <td>subDescription</td>
                        <td>String</td>
                        <td>否</td>
                        <td>-</td>
                        <td>业务返回码详细描述,详见文档</td>
                        <td>交易已被支付</td>
                    </tr>
                    <tr>
                        <td>nonceStr</td>
                        <td>String</td>
                        <td>是</td>
                        <td>-</td>
                        <td>与请求随机字符串相同</td>
                        <td>2jSuRZ0jQFf3Z5Em</td>
                    </tr>
                    <tr>
                        <td>result</td>
                        <td>String</td>
                        <td>是</td>
                        <td>-</td>
                        <td>返回业务数据</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>sign</td>
                        <td>String</td>
                        <td>是</td>
                        <td>-</td>
                        <td>签名,<a href="sign.html">详见文档</a></td>
                        <td>DZXh8eeTuAHoYE3w1J+POiPhfDxOYBfUNn1lkeT/V7P4zJdyojWEa6IZs6Hz0yDW5Cp/viufUb5I0/V5WENS3OYR8zRedqo6D+fUTdLHdc+EFyCkiQhBxIzgngPdPdfp1PIS7BdhhzrsZHbRqb7o4k3Dxc+AAnFauu4V6Zdwczo=</td>
                    </tr>
                </table>
            </div>
            <!-- ./公共响应参数 -->

            <!-- 响应参数 -->
            <div class="data response">
                <h5>响应参数</h5>
                <table>
                    <tr>
                        <th>参数</th>
                        <th>类型</th>
                        <th>是否必填</th>
                        <th>最大长度</th>
                        <th>描述</th>
                        <th>示例值</th>
                    </tr>
                    <#list responseParameters as responseParameter>
                    <#if responseParameter.type == 'Array' || responseParameter.type == 'Object'>
                    <tr class="trigger">
                        <td>
                            <span>
                                <i class="fa fa-plus-square-o"></i>
                                <i class="fa fa-minus-square-o hide"></i>
                            </span>
                            ${responseParameter.name}
                        </td>
                        <td>${responseParameter.type!''}</td>
                        <td>${responseParameter.required?string('是','否')}</td>
                        <td>${responseParameter.maxLength!'-'}</td>
                        <td>${responseParameter.description}</td>
                        <td>${responseParameter.sample!''}</td>
                    </tr>
                    <tr class="trigger-data hide">
                        <td colspan="6">
                            <#list responseParameter.parameters as parameter>
                            <ul>
                                <li><span>${parameter.name}</span></li>
                                <li><span>${parameter.type}</span></li>
                                <li><span>${parameter.required?string('是','否')}</span></li>
                                <li><span>${parameter.maxLength!'-'}</span></li>
                                <li><span>${parameter.description}</span></li>
                                <li><span>${parameter.sample!''}</span></li>
                            </ul>
                            </#list>
                        </td>
                    </tr>

                    <#else>
                    <tr>
                        <td>${responseParameter.name}</td>
                        <td>${responseParameter.type!''}</td>
                        <td>${responseParameter.required?string('是','否')}</td>
                        <td>${responseParameter.maxLength!'-'}</td>
                        <td>${responseParameter.description}</td>
                        <td>${responseParameter.sample!''}</td>
                    </tr>
                    </#if>
                    </#list>
                </table>
            </div>
            <!-- ./响应参数 -->

            <!-- 业务错误码 -->
            <div class="data error">
                <h5>业务错误码</h5>
                <table>
                    <tr>
                        <th>错误码</th>
                        <th>错误描述</th>
                        <th>解决方案</th>
                    </tr>
                    <tr>
                        <td>ACQ.SYSTEM_ERROR</td>
                        <td>系统错误</td>
                        <td>重新发起请求</td>
                    </tr>
                    <tr>
                        <td>ACQ.INVALID_PARAMETER</td>
                        <td>参数无效</td>
                        <td>检查请求参数，修改后重新发起请求</td>
                    </tr>
                    <tr>
                        <td>ACQ.TRADE_NOT_EXIST</td>
                        <td>查询的交易不存在</td>
                        <td>检查传入的交易号是否正确，修改后重新发起请求</td>
                    </tr>
                </table>
            </div>
            <!-- ./业务错误码 -->

        </div>
        <!-- content -->
    </div>
    <script src="https://cdn.bootcss.com/jquery/2.2.4/jquery.min.js"></script>
    <script src="js/index.js"></script>
</body>

</html>