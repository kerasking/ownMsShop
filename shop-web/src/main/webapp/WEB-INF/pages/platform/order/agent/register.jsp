<%@ page language="java" import="java.util.*" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <title>麦链合伙人</title>
    <%@ include file="/WEB-INF/pages/common/head.jsp" %>
    <link rel="stylesheet" href="${path}/static/css/zhuce.css">
</head>
<body>
<div class="wrap">
    <div id="box">
        <header class="xq_header">
            <a href="${basePath}userApply/apply.shtml?skuId=${skuId}">
                <img src="${path}/static/images/xq_rt.png" alt=""></a>
            <p>申请合伙人</p>
        </header>
        <div class="xinxi">
            <p>注册信息</p>
            <p>确认订单</p>
            <p>完成合伙</p>
        </div>
        <c:if test="${isQueuing==true}">
            <p class="row">本次订单将进入排单期，在您前面有<span>${count}</span>人排单。</p>
        </c:if>
        <p class="xuanze">
            选择商品：<span>${skuName}</span>
        </p>
        <main>
            <section class="sec1">
                <div>
                    <p>微信号：
                        <input type="text" class="wei" id="weixin" name="weixin"/>
                        <span class="onc"></span><b class="gao"></b>
                    </p>
                    <h1>*此微信号将在授权证书上显示</h1>
                </div>
            </section>
            <section class="sec2">
                <c:if test="${pWxNkName==''}">
                    <h3 class="tui" style="display: block">
                        <span style="font-size: 12px;">是否有推荐人：</span>
                        <input type="radio" id="q" name="danx" class="shi" checked="checked">
                        <label for="q" style="margin-left:5px;">是</label>
                        <input type="radio" id="b" name="danx" class="fou"/>
                        <label for="b" style="margin-left:30px;">否</label>
                    </h3>
                    <div id="hehuo">
                        <h3>上级合伙人电话： <input type="text" class="stel" id="pMobile" name="pMoble">
                            <span class="onc"></span>
                            <b class="gao"></b>
                        </h3>
                    </div>
                </c:if>
                <c:if test="${pWxNkName!=''}">
                    <div id="hehuo">
                        <h3 class="Bhide">您的推荐人：${pWxNkName}</h3>
                    </div>
                </c:if>

                <h2>选择合伙人套餐：</h2>
                <%--<div class="dengji">--%>
                <%--<c:forEach items="${agentSkuViews}" var="view">--%>
                <%--<c:if test="${view.agent.agentLevelId >= pUserLevelId--%>
                <%--&& view.isShow==1--%>
                <%--&& (agentLevelIds.equals('') || fn:contains(agentLevelIds, view.agent.agentLevelId.toString().concat(',')))}">--%>
                <%--<p levelId="${view.agent.agentLevelId}"--%>
                <%--agentFee="${view.agentFee}"--%>
                <%--agentBailFee="${view.agent.bail}">--%>
                <%--<span>${view.agentFee}元套餐</span>--%>
                <%--<span>保证金：${view.agent.bail}元</span>--%>
                <%--<span>包含商品：${view.agent.quantity}件</span>--%>
                <%--</p>--%>
                <%--</c:if>--%>
                <%--</c:forEach>--%>
                <%--</div>--%>
                <div class="dengji">
                    <c:forEach items="${agentSkuViews}" var="view">
                        <c:if test="${view.agent.agentLevelId >= pUserLevelId
                    && view.isShow==1
                    && (agentLevelIds.equals('') || fn:contains(agentLevelIds, view.agent.agentLevelId.toString().concat(',')))}">
                            <div class="floor"
                                 levelId="${view.agent.agentLevelId}"
                                 agentFee="${view.agentFee}"
                                 agentBailFee="${view.agent.bail}">
                                <h1>${view.agentFee}元套餐</h1>
                                <p>
                                    <span>等级：</span>
                                    <span>${view.level.name}</span>
                                </p>
                                <p>
                                    <span>单价：</span>
                                    <span>${view.agent.unitPrice}</span>
                                </p>
                                <p>
                                    <span>数量：</span>
                                    <span>${view.agent.quantity}</span>
                                </p>
                                    <%--<p>--%>
                                    <%--<span>总价：</span>--%>
                                    <%--<span>${view.agentFee}</span>--%>
                                    <%--</p>--%>
                                <p>
                                    <span>保证金：</span>
                                    <span>${view.agent.bail}</span>
                                </p>
                                <h2>
                                </h2>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
            </section>
            <section class="sec3">
                <p>
                    <!--<input type="checkbox" id="fu" checked disabled>-->
                    <label>我已同意<a href="#" class="daili">《代理商注册协议》</a></label>
                </p>
            </section>
            <section class="sec4">
                <a id="next" style="color:white" href="javascript:void(0);">下一步</a>
            </section>
        </main>
    </div>
    <div class="back_que">
        <p>数据确认</p>
        <h4><b>商品选择:</b><span id="q_skuName">${skuName}</span></h4>
        <h4><b>微信:</b><span id="q_weixinId"></span></h4>
        <c:choose>
            <c:when test="${pWxNkName==''}">
                <h4><b>推荐人电话:</b><span id="q_pMobile"></span></h4>
            </c:when>
            <c:otherwise>
                <h4><b>推荐人微信:</b><span id="q_pWx">${pWxNkName}</span></h4>
            </c:otherwise>
        </c:choose>
        <h4><b>合伙人套餐:</b><span id="q_levelName"></span></h4>
        <h4><b>拿货门槛:</b><span id="q_amount"></span></h4>
        <h3>
            <span class="que_qu" id="getBack">返回修改</span>
            <span class="que_que" id="submit">我填的正确</span>
        </h3>
    </div>
    <div class="back"></div>
    <div class="paidanqi">
        <div class="back_q">
            <h1>什么是排单期？</h1>
            <p>
                由于商品过于火爆，导致库存量不足。申请合伙人或补货我们将记录付款的先后顺序，待产能提升，麦链商城将按照付款顺序发货
            </p>
            <button class="kNow">我知道了</button>
        </div>
        <div class="Modal"></div>
    </div>
    <div class="xieyi">
        <div class="back_q" style="margin-top:-166px">
            <h2 style="text-align: center;padding: 5px">麦链合伙人服务条款</h2>
            <p style="height: 200px;overflow-y: auto;">
                麦链合伙人服务条款

                麦链合伙人依据以下条件和条款为您提供所享有的服务，请仔细阅读并遵守。
                欢迎阅读麦链合伙人服务条款协议(下称“本协议”)。本协议阐述之条款和条件适用于您使用麦链合伙人，所提供的在电子市场中进行贸易和交流的各种工具和服务(下称“服务”)。

                一、接受条款
                1、本协议内容包括协议正文及所有麦链合伙人已经发布或将来可能发布的各类规则。所有规则为协议不可分割的一部分，与协议正文具有同等法律效力。
                2、以任何方式进入麦链合伙人并使用服务即表示您已充分阅读、理解并同意接受本协议的条款和条件(以下合称“条款”)。
                3、麦链合伙人有权根据业务需要酌情修订“条款”，并以公告的形式进行更新，不再单独通知予您。经修订的“条款”一经在麦链合伙人公布，即产生效力。如您不同意相关修订，请您立即停止使用“服务”。如您继续使用“服务”，则将视为您已接受经修订的“条款”，当您与麦链合伙人发生争议时，应以最新的“条款”为准。

                二、注册
                1、服务使用对象
                您确认，在您完成注册程序或以其他麦链合伙人的方式实际使用服务时，您应当是具备完全民事权利能力和与所从事的民事行为相适应的行为能力的自然人、法人或其他组织。若您不具备前述主体资格，请勿使用服务，否则您及您的监护人应承担因此而导致的一切后果，且麦链合伙人有权注销（永久冻结）您的账户，并向您及您的监护人索偿。如您代表一家公司或其他法律主体在麦链合伙人登记，则您声明和保证，您有权使该公司或该法律主体受本协议“条款”的约束。
                2、注册账户
                2.1在您按照注册页面提示填写信息、阅读并同意本协议且完成全部注册程序后，或在您按照激活页面提示填写信息、阅读并同意本协议且完成全部激活程序后，或您以其他麦链合伙人允许的方式实际使用麦链合伙人服务时，您即受本协议的约束。您可以使用您提供或确认的邮箱、手机号码或者麦链合伙人的其它方式作为登录手段进入麦链合伙人。
                2.2您了解并同意，如您系在本平台完成的注册程序，只要您注册成功，您即可以获得您的登录名。您可以通过您的登录名直接登录麦链合伙人平台。
                2.3您可以对账户设置昵称，但您设置的昵称不得侵犯或涉嫌侵犯他人合法权益。如您设置的昵称涉嫌侵犯他人合法权益，麦链合伙人有权终止向您提供服务，并注销您的账户。账户注销后，相应的昵称将开放给其他有权用户登记使用。
                2.4在完成注册或激活流程时，您应当按照法律法规要求，按相应页面的提示准确提供并及时更新您的资料，以使之真实、及时，完整和准确。如有合理理由怀疑您提供的资料错误、不实、过时或不完整的，麦链合伙人有权向您发出询问及/或要求改正的通知，并有权直接做出删除相应资料的处理，直至中止、终止对您提供部分或全部麦链合伙人服务，麦链合伙人对此不承担任何责任，您将承担因此产生的任何直接或间接损失及不利后果。

                三、账户安全
                您须自行负责对您的登录名、昵称和密码保密，且须对该登录名、昵称和密码下发生的所有活动（包括但不限于信息披露、发布信息、网上点击同意或提交各类规则协议、续签协议或购买服务等）承担责任。您同意：(a)如发现任何人未经授权使用您的登录名、昵称和密码，或发生违反保密规定的任何其他情况，您会立即通知麦链合伙人，并授权麦链合伙人将该信息同步；(b)确保您在每个上网时段结束时，以正确步骤离开平台。麦链合伙人不能也不会对因您未能遵守本款规定而发生的任何损失或损毁负责。您理解麦链合伙人对您的请求采取行动需要合理时间，麦链合伙人对在采取行动前已经产生的后果（包括但不限于您的任何损失）不承担任何责任。除非有法律规定或司法裁定，且征得麦链合伙人的同意，否则，您的登录名、昵称和密码不得以任何方式转让、赠与或继承（与账户相关的财产权益除外）。

                四、服务终止
                1、服务终止：
                1.1您同意，在麦链合伙人未向您收费的情况下，麦链合伙人可自行全权决定以任何理由(包括但不限于麦链合伙人认为您已违反本协议的字面意义和精神，或您以不符合本协议的字面意义和精神的方式行事)终止您的“服务”密码、账户(或其任何部份)或您对“服务”的使用。您同意，在麦链合伙人向您收费的情况下，麦链合伙人应基于合理的怀疑且经电子邮件通知的情况下实施上述终止服务的行为。您进一步承认和同意，麦链合伙人根据本协议规定终止您服务的情况下可立即使您的账户无效，或注销您的账户以及在您的账户内的所有相关资料和档案，和/或禁止您进一步接入该等档案或“服务”。账户终止后，麦链合伙人没有义务为您保留原账户中或与之相关的任何信息，或转发任何未曾阅读或发送的信息给您或第三方。此外，您同意，麦链合伙人不会就终止向您提供“服务”而对您或任何第三者承担任何责任。
                1.2您有权向麦链合伙人要求注销您的账户，经麦链合伙人审核同意的，麦链合伙人将注销您的账户，届时，您与麦链合伙人基于本协议的合同关系即终止。您的账户被注销后，麦链合伙人没有义务为您保留或向您披露您账户中的任何信息，也没有义务向您或第三方转发任何您未曾阅读或发送过的信息。
                1.3.您理解并同意，您与麦链合伙人的合同关系终止后：
                a)麦链合伙人有权继续保存您的资料。
                b)您在使用服务期间存在违法行为或违反本协议和/或规则的行为的，麦链合伙人仍可依据本协议向您主张权利。
                C）您在使用服务期间因使用服务与其他用户之间发生的关系，不因本协议的终止而终止，其他用户仍有权向您主张权利，您应继续按您的承诺履行义务。

                五、关于费用。
                麦链合伙人保留在根据第一条第3款通知您后，收取“服务”费用的权利。另外，您因进行交易、向麦链合伙人获取有偿服务或接触麦链合伙人服务器而发生的所有应纳税赋，以及相关硬件、软件、通讯、网络服务及其他方面的费用均由您自行承担。麦链合伙人保留在无须发出书面通知，仅在平台公示的情况下，暂时或永久地更改或停止部分或全部“服务”的权利。

                六、服务使用规范
                1、关于您的资料的规则
                1.1“您的资料”包括您在注册、发布信息或交易等过程中、在任何公开信息场合或通过任何电子邮件形式，向麦链合伙人或其他用户提供的任何资料
                ，包括数据、文本、软件、音乐、声响、照片、图画、影像、词句或其他材料。您应对“您的资料”负全部责任，而麦链合伙人仅作为您在网上发布和刊登“您的资料”的被动渠道。
                1.2您同意并承诺，“您的资料”和您供在麦链合伙人上交易的任何“物品”（泛指一切可供依法交易的、有形的或无形的、以各种形态存在的某种具体的物品，或某种权利或利益，或某种票据或证券，或某种服务或行为。本协议中“物品”一词均含此义）:
                a.不会有欺诈成份，与售卖伪造或盗窃无涉；
                b.不会侵犯任何第三者对该物品享有的物权，或版权、专利、商标、商业秘密或其他知识产权，或隐私权、名誉权；
                c.不会违反任何法律、法规、条例或规章(包括但不限于关于规范出口管理、凭许可证经营、贸易配额、保护消费者、不正当竞争或虚假广告的法律、法 规、条例或规章)、本协议及相关规则；
                d.不会含有诽谤（包括商业诽谤）、非法恐吓或非法骚扰的内容；
                e.不会含有淫秽、或包含任何儿童色情内容；
                f.不会含有蓄意毁坏、恶意干扰、秘密地截取或侵占任何系统、数据或个人资料的任何病毒、伪装破坏程序、电脑蠕虫、定时程序炸弹或其他电脑程序；
                g.不会直接或间接与下述各项货物或服务连接，或包含对下述各项货物或服务的描述：(i)本协议项下禁止的货物或服务；或(ii)您无权连接或包含的货物或服务。此外，您同意不会：（ⅲ）在与任何连锁信件、大量胡乱邮寄的电子邮件、滥发电子邮件或任何复制或多余的信息有关的方面使用“服务”；(ⅳ)未经其他人士同意，利用“服务”收集其他人士的电子邮件地址及其他资料；或（ⅴ）利用“服务”制作虚假的电子邮件地址，或以其他形式试图在发送人的身份或信息的来源方面误导其他人士；
                h.不含有麦链合伙人认为应禁止或不适合通过麦链合伙人宣传或交易。
                1.3您同意，您不会对任何资料作商业性利用，包括但不限于在未经麦链合伙人事先书面批准的情况下，复制在麦链合伙人平台上展示的任何资料并用于商业用途。
                2、关于交易的规则
                2.1添加产品描述条目。产品描述是由您提供的在麦链合伙人上展示的文字描述、图画和/或照片，可以是(a)对您拥有而您希望出售的产品的描述；或(b)对您正寻找的产品的描述。您须将该等产品描述归入正确的类目内。麦链合伙人不对产品描述的准确性或内容负责。
                2.2就交易进行协商。交易各方通过在麦链合伙人上明确描述报盘和回盘，进行相互协商。所有各方接纳报盘或回盘将使所涉及的麦链合伙人用户有义务完成交易。除非在特殊情况下，诸如用户在您提出报盘后实质性地更改对物品的描述或澄清任何文字输入错误，或您未能证实交易所涉及的用户的身份等，报盘和承诺均不得撤回。
                2.3不得操纵交易。您同意不利用帮助实现蒙蔽或欺骗意图的同伙(下属的客户或第三方)，操纵与另一交易方所进行的商业谈判的结果。
                2.4不得干扰交易系统。您同意，您不得使用任何装置、软件或例行程序干预或试图干预麦链合伙人的正常运作或正在麦链合伙人上进行的任何交易。您不得采取对任何将不合理或不合比例的庞大负载加诸麦链合伙人结构的行动。
                2.5关于交易反馈。您不得采取任何可能损害信息反馈系统的完整性的行动，诸如：利用第二会员身份标识或第三者为您本身留下正面反馈；利用第二会员身份标识或第三者为其他用户留下负面反馈(反馈数据轰炸)；或在用户未能履行交易范围以外的某些行动时，留下负面的反馈(反馈恶意强加)。
                2.6关于处理交易争议。
                (i)麦链合伙人不涉及用户间因交易而产生的法律关系及法律纠纷，不会且不能牵涉进交易各方的交易当中。倘若您与一名或一名以上用户，或与您通过麦链合伙人获取其服务的第三者服务供应商发生争议，您免除麦链合伙人(及麦链合伙人代理人和雇员)在因该等争议而引起的，或在任何方面与该等争议有关的不同种类和性质的任何(实际和后果性的)权利主张、要求和损害赔偿等方面的责任。
                (ii)麦链合伙人有权受理并调处您与其他用户因交易产生的争议，同时有权单方面独立判断其他用户对您的投诉及(或)索偿是否成立，若麦链合伙人判断索偿成立，则您应及时使用自有资金进行偿付，否则麦链合伙人有权使用您交纳的保证金（如有）或扣减已购麦链合伙人及其关联公司的产品或服务中未履行部分的费用的相应金额或您在麦链合伙人所有账户下的其他资金(或权益)等进行相应偿付。麦链合伙人没有使用自用资金进行偿付的义务，但若进行了该等支付，您应及时赔偿麦链合伙人的全部损失，否则麦链合伙人有权通过前述方式抵减相应资金或权益，如仍无法弥补麦链合伙人损失，则麦链合伙人保留继续追偿的权利。因麦链合伙人非司法机构，您完全理解并承认，麦链合伙人对证据的鉴别能力及对纠纷的处理能力有限，受理贸易争议完全是基于您之委托，不保证争议处理结果符合您的期望，亦不对争议处理结果承担任何责任。麦链合伙人有权决定是否参与争议的调处。
                (iii)麦链合伙人有权通过电子邮件等联系方式向您了解情况，并将所了解的情况通过电子邮件等方式通知对方，您有义务配合麦链合伙人的工作，否则麦链合伙人有权做出对您不利的处理结果。
                3、违反规则的后果。
                3.1倘若麦链合伙人认为“您的资料”可能使麦链合伙人承担任何法律或道义上的责任，或可能使麦链合伙人(全部或部分地)失去麦链合伙人的互联网服务供应商或其他供应商的服务，则麦链合伙人可自行全权决定对“您的资料”采取麦链合伙人认为必要或适当的任何行动，包括但不限于删除该类资料。您特此保证，您对提交给麦链合伙人的“您的资料”拥有全部权利，包括全部版权。您确认，麦链合伙人没有责任去认定或决定您提交给麦链合伙人的资料哪些是应当受到保护的，对享有“服务”的其他用户使用“您的资料”，麦链合伙人也不必负责。
                3.2对于您涉嫌违反承诺的行为对任意第三方造成损害的，您均应当以自己的名义独立承担所有的法律责任，并应确保麦链合伙人免责。
                3.3在不限制其他补救措施的前提下，发生下述任一情况，麦链合伙人可立即发出警告，暂时中止、永久中止或终止您的会员资格，删除您的任何现有产品信息，以及您在网站上展示的任何其他资料：(i)您违反本协议；(ii)麦链合伙人无法核实或鉴定您向麦链合伙人提供的任何资料；或(iii)麦链合伙人相信您的行为可能会使您、麦链合伙人用户或通过麦链合伙人提供服务的第三者服务供应商发生任何法律责任。在不限制任何其他补救措施的前提下，若发现您从事涉及麦链合伙人的诈骗活动，麦链合伙人可暂停或终止您的账户。
                3.4经生效法律文书确认用户存在违法或违反本协议行为或者麦链合伙人自行判断用户涉嫌存在违法或违反本协议行为的，麦链合伙人有权在麦链合伙人平台上以网络发布形式公布用户的违法行为并做出处罚（包括但不限于限权、终止服务等）。

                七、您授予的许可使用权。
                您完全理解并同意不可撤销地授予麦链合伙人及其关联公司下列权利：
                1、对于您提供的资料，您授予麦链合伙人及其关联公司独家的、全球通用的、永久的、免费的许可使用权利(并有权在多个层面对该权利进行再授权)，使麦链合伙人及其关联公司有权(全部或部份地)使用、复制、修订、改写、发布、翻译、分发、执行和展示"您的资料"或制作其派生作品，和/或以现在已知或日后开发的任何形式、媒体或技术，将"您的资料"纳入其他作品内。
                2、当您违反本协议或与麦链合伙人签订的其他协议的约定，麦链合伙人有权以任何方式通知关联公司，要求其对您的权益采取限制措施包括但不限于要求将您账户内的款项支付给麦链合伙人指定的对象，要求关联公司中止、终止对您提供部分或全部服务，且在其经营或实际控制的任何平台公示您的违约情况。
                3、同样，当您向麦链合伙人关联公司作出任何形式的承诺，且相关公司已确认您违反了该承诺，则麦链合伙人有权立即按您的承诺约定的方式对您的账户采取限制措施，包括但不限于中止或终止向您提供服务，并公示相关公司确认的您的违约情况。您了解并同意，麦链合伙人无须就相关确认与您核对事实，或另行征得您的同意，且麦链合伙人无须就此限制措施或公示行为向您承担任何的责任。

                八、隐私。
                基于保护您的隐私是麦链合伙人的一项基本原则，为此麦链合伙人还将根据麦链合伙人的隐私声明使用"您的资料"。麦链合伙人隐私声明的全部条款属于本协议的一部份，因此，您必须仔细阅读。请注意，您一旦自愿地在麦链合伙人交易地点披露"您的资料"，该等资料即可能被其他人士获取和使用。

                九、责任范围和责任限制。
                1、您明确理解和同意，麦链合伙人不对因下述任一情况而发生的任何损害赔偿承担责任，包括但不限于利润、商誉、使用、数据等方面的损失或其他无形损失的损害赔偿(无论麦链合伙人是否已被告知该等损害赔偿的可能性)：(i)使用或未能使用“服务”；(ii)因通过或从“服务”购买或获取任何货物、样品、数据、资料或服务，或通过或从“服务”接收任何信息或缔结任何交易所产生的获取替代货物和服务的费用；(iii)未经批准接入或更改您的传输资料或数据；(iv)任何第三者对“服务”的声明或关于“服务”的行为；或(v)因任何原因而引起的与“服务”有关的任何其他事宜，包括疏忽。
                2、麦链合伙人会尽一切努力使您在使用麦链合伙人平台的过程中得到乐趣。遗憾的是，麦链合伙人不能随时预见到任何技术上的问题或其他困难。该等困难可能会导致数据损失或其他服务中断。为此，您明确理解和同意，您使用“服务”的风险由您自行承担，且“服务”以“按现状”和“按可得到”的状态提供。麦链合伙人明确声明不作任何种类的明示或暗示的保证，包括但不限于关于适销性、适用于某一特定用途和无侵权行为等方面的保证。麦链合伙人对下述内容不作保证：(i)“服务”会符合您的要求；(ii)“服务”不会中断，且适时、安全和不带任何错误；(iii)通过使用“服务”而可能获取的结果将是准确或可信赖的；及(iv)您通过“服务”而购买或获取的任何产品、服务、资料或其他材料的质量将符合您的预期。通过使用“服务”而下载或以其他形式获取任何材料是由您自行全权决定进行的，且与此有关的风险由您自行承担，对于因您下载任何该等材料而发生的您的电脑系统的任何损毁或任何数据损失，您将自行承担责任。您从麦链合伙人或通过或从“服务”获取的任何口头或书面意见或资料，均不产生未在本协议内明确载明的任何保证责任。

                十.赔偿。
                您同意，如因您违反本协议或经在此提及而纳入本协议的其他文件，或因您违反法律侵害了第三方的合法权利，或因您违反法律须承担行政或刑事责任，而使第三方或行政、司法机关对麦链合伙人及其子公司、关联公司、分公司、董事、职员、代理人提出索赔或处罚要求（包括司法费用和其他专业人士的费用），您必须全额赔偿给麦链合伙人及其子公司、关联公司、分公司、董事、职员、代理人，并使其等免遭损失。

                十一、链接。
                “服务”或第三者均可提供与其他万维网网站或资源的链接。由于麦链合伙人并不控制该等网站和资源，您承认并同意，麦链合伙人并不对该等外在网站或资源的可用性负责，且不认可该等网站或资源上或可从该等网站或资源获取的任何内容、宣传、产品、服务或其他材料，也不对其等负责或承担任何责任。您进一步承认和同意，对于任何因使用或信赖从此类网站或资源上获取的此类内容、宣传、产品、服务或其他材料而造成（或声称造成）的任何直接或间接损失，麦链合伙人均不承担责任。

                十二、通知。
                1、您应当准确填写并及时更新您提供的电子邮件地址、联系电话、联系地址、邮政编码等联系方式，以便麦链合伙人或其他用户与您进行有效联系，因通过这些联系方式无法与您取得联系，导致您在使用麦链合伙人服务过程中产生任何损失或增加费用的，应由您完全独自承担。您了解并同意，您有义务保持你提供的联系方式的有效性，如有变更需要更新的，您应按麦链合伙人的要求进行操作。
                2、除非另有明确规定，任何您与麦链合伙人之间的通知应以电子邮件形式发送，(就麦链合伙人而言)电子邮件地址为jackie.wang@iimai.com，或(就您而言)发送到您在登记注册过程中向麦链合伙人提供的电子邮件地址，或有关方指明的该等其他地址。在电子邮件发出二十四(24)小时后，通知应被视为已送达，除非发送人被告知相关电子邮件地址已作废。或者，麦链合伙人可通过邮资预付挂号邮件并要求回执的方式，将通知发到您在登记过程中向麦链合伙人提供的地址。在该情况下，在付邮当日三(3)天后通知被视为已送达。

                十三、不可抗力。
                对于因麦链合伙人合理控制范围以外的原因，包括但不限于自然灾害、罢工或骚乱、物质短缺或定量配给、暴动、战争行为、政府行为、通讯或其他设施故障或严重伤亡事故等，致使麦链合伙人延迟或未能履约的，麦链合伙人不对您承担任何责任。
                十四、关店铺零售
                1.店主一旦选择店主发货，系统会自动生成一条“店主发货”的商品，可自行设置库存，店主需合理设置库存。
                2.店主发货方式下，接到用户订单，店主需自行及时处理用户订单，以免影响用户体验。
                3.若用户发生退换货行为，店主需耐心服务用户，线下协调处理，与平台台无关。
                4.店主发货方式的商品与平台无关，平台无需承担任何责任
                5.按照国家法律，零售需要缴纳零售额的17%。麦链平台会补助店主，所以需要店主缴纳零售额的10%作为税收，由平台代扣、代缴。此税只适用于零售，代理之间拿货不受影响。
                十五.法律适用、管辖及其他
                1、本协议之效力、解释、变更、执行与争议解决均适用中华人民共和国大陆地区法律，如无相关法律规定的，则应参照通用国际商业惯例和（或）行业惯例。
                2、您与麦链合伙人仅为独立订约人关系。本协议无意结成或创设任何代理、合伙、合营、雇佣与被雇佣或特性授权与被授权关系。
                3、您同意麦链合伙人因经营业务需要有权将本协议项下的权力义务就部分或全部进行转让，而无须再通知予您并取得您的同意。
                4、因本协议或麦链合伙人服务所引起或与其有关的任何争议应向麦链合伙人所在地人民法院提起诉讼。
                5、本协议取代您和麦链合伙人先前就相同事项订立的任何书面或口头协议。倘若本协议任何条款被裁定为无效或不可强制执行，该项条款应被撤销，而其余条款应予遵守和执行。条款标题仅为方便参阅而设，并不以任何方式界定、限制、解释或描述该条款的范围或限度。麦链合伙人未就您或其他人士的某项违约行为采取行动，并不表明麦链合伙人撤回就任何继后或类似的违约事件采取动的权利。
            </p>
            <button class="kNow">我知道了</button>
        </div>
        <div class="Modal"></div>
    </div>
</div>

</body>
<%@ include file="/WEB-INF/pages/common/foot.jsp" %>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="${path}/static/js/hideWXShare.js"></script>
<script>
    var path = "${basePath}";
    var skuId = "${skuId}";
    var pUserId = "${pUserId}";
    var sendType = "${sendType}";
</script>
<script src="${path}/static/js/zhuceUtil.js"></script>
</html>