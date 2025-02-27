<%@ page language="java" import="java.util.*" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="com.masiis.shop.web.common.utils.ApplicationContextUtil" %>
<%@ page import="com.masiis.shop.scheduler.task.platform.PfOrderTask" %>
<%@ page import="com.masiis.shop.scheduler.task.mall.SfOrderTask" %>
<%@ page import="com.masiis.shop.scheduler.task.mall.SfShopTask" %>
<%@ page import="com.masiis.shop.scheduler.task.platform.PfUpgradeTask" %>
<%@ page import="com.masiis.shop.scheduler.task.platform.PfCertificateTask" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta charset="utf-8">
</head>
<body>
<%
    String type = request.getParameter("type");
    if(StringUtils.isBlank(type)){
        return;
    }
    ApplicationContext ac = ApplicationContextUtil.getContext();
    if(type.equals("pfcount")){
        PfOrderTask pfOrderTask = (PfOrderTask) ac.getBean("pfOrderTask");
        pfOrderTask.billCountJob();
    } else if(type.equals("sfcount")){
        SfOrderTask sfOrderTask = (SfOrderTask) ac.getBean("sfOrderTask");
        sfOrderTask.sfBillCountJob();
    } else if(type.equals("offline")){
        PfOrderTask pfOrderTask = (PfOrderTask) ac.getBean("pfOrderTask");
        pfOrderTask.offlineBorderCancelJob();
    } else if(type.equals("sfshop")){
        SfShopTask pfOrderTask = (SfShopTask) ac.getBean("sfShopTask");
        pfOrderTask.sfShopBillCountJob();
    } else if(type.equals("upunsolved")){
        PfUpgradeTask upgradeTask = (PfUpgradeTask) ac.getBean("pfUpgradeTask");
        upgradeTask.unHandleUpgradeNoticeJob();
    } else if(type.equals("upunpayTwo")){
        PfUpgradeTask upgradeTask = (PfUpgradeTask) ac.getBean("pfUpgradeTask");
        upgradeTask.upgradeNoticeTwoDayUnPay();
    } else if(type.equals("upunpaySeven")){
        PfUpgradeTask upgradeTask = (PfUpgradeTask) ac.getBean("pfUpgradeTask");
        upgradeTask.upgradeNoticeSevenDayUnPay();
    } else if(type.equals("queueorder")){
        PfOrderTask pfOrderTask = (PfOrderTask) ac.getBean("pfOrderTask");
        pfOrderTask.doSomethingWithDelay();
    } else if(type.equals("sfautoconfirm")){
        SfOrderTask sfOrderTask = (SfOrderTask) ac.getBean("sfOrderTask");
        sfOrderTask.orderReceivedConfirmJob();
    } else if(type.equals("pfcert")){
        PfCertificateTask task = (PfCertificateTask) ac.getBean("pfCertificateTask");
        task.autoCreateCertificateJob();
    }
%>
</body>
</html>
