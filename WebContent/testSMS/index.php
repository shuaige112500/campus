<?php
    $ch = curl_init();
    $url = 'http://apis.baidu.com/sms_net/sms_net_sdk/sms_net_sdk?content=%E7%9F%AD%E4%BF%A1%E7%9F%AD%E4%BF%A1%E6%8E%A5%E5%8F%A3%E9%AA%8C%E8%AF%81%E7%A0%811111&mobile=18516339933';
    $header = array(
        'apikey: 59a2cbd4d5bf4569300653fdd1a9bd89',
    );
    // 添加apikey到header
    curl_setopt($ch, CURLOPT_HTTPHEADER  , $header);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    // 执行HTTP请求
    curl_setopt($ch , CURLOPT_URL , $url);
    $res = curl_exec($ch);

    var_dump(json_decode($res));
?>