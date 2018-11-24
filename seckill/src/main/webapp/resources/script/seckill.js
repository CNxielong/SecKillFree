// 存放主要交互逻辑js代码
//  javascript 模块化

// seckill.detail.init(params);
var seckill = {

    // 封装秒杀相关的ajax的url
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }
    },

    // 获取秒杀地址,控制显示器,执行秒杀
    handleSeckkillkill: function(seckillId, node){

        node.html('<button class="btn btn-primary btn-lg" id="killBtn" > 开始秒杀</button>');

        $.post(seckill.URL.exposer(seckillId), {}, function(result){
            // 在回调函数中执行交互流程
            if( result && result.success){// result存在并且result.success为true
                var exposer =  result.data;
                if( exposer.exposed){ // 如果开启了秒杀
                    var md5 = exposer.md5;// 获取md5
                    var killUrl = seckill.URL.execution(seckillId,md5);// 获取URL 然后执行秒杀事件
                    console.log("killUrl:"+killUrl);
                    // 绑定一次点击事件
                    $('#killBtn').one('click',function(){
                        console.log("绑定了一次点击事件");
                        // 执行秒杀请求
                        // 1.先禁用按钮
                        $(this).addClass('disabled');// ,<-$(this)===('#killBtn')->
                        // 2.发送秒杀请求执行秒杀
                        $.post(killUrl,{},function (result) {// 局部变量会覆盖
                            console.log(result)
                            if(result && result.success){ // 如果秒杀成功
                                var  SeckillExecution = result.data;// 获取返回的结果数据
                                var stateInfo = SeckillExecution.stateInfo;
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }else{
                                console.log("result:"+result);
                            }
                        });
                    });
                    
                }else{//秒杀未开启
                  var now =  exposer.now;
                  var start = exposer.start;
                  var end = exposer.end;
                  seckill.countdown(seckillId,now,start,end);// 倒计时
                }
            }else{
                console.log("result:"+result);
            }
        });
    },

    // 验证手机号
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            // console.log('验证通过');
            return true; // 直接判断对象会看对象是否为空,空就是undefine就是false; isNaN 非数字返回true
        } else {
            return false;
        }
    },

    // 验证时间
    countdown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $("#secjukk-box");
        if (nowTime > endTime) {
            // 秒杀结束
            seckillBox.html('秒杀结束');
        } else if (nowTime < startTime) {
            // 秒杀未开始,计时事件绑定
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                // 时间格式
                var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown',function () {
                // 获取秒杀地址, 控制显示逻辑，执行秒杀
                seckill.handleSeckkillkill(seckillId, seckillBox);
            }) ;
            seckillBox.countdown();
        } else if (nowTime > startTime) {
            // 秒杀开始
            seckill.handleSeckkillkill(seckillId, seckillBox);
        }
    },

    // 详情页秒杀逻辑
    detail: {
        // 详情页初始化
        init: function (params) {
            // 手机验证和登录，计时交互

            // 规划我们的交互流程
            // 在cookie中查找手机号
            var killPhone = $.cookie('killPhone');

            // 验证手机号
            if (!seckill.validatePhone(killPhone)) {
                // console.log("手机号码验证没有通过");
                //绑定手机 控制输出
                var killPhoneModal = $('#killPhoneModel');
                killPhoneModal.modal({
                    show: true,// 显示弹出层
                    backdrop: 'static',// 禁止位置关闭
                    keyboard: false// 关闭键盘事件
                });

                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    console.log("inputPhone:" + inputPhone);
                    if (seckill.validatePhone(inputPhone)) {
                        // 通过验证 电话写入cookie
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        // 验证通过  刷新页面
                        window.location.reload();
                    } else {
                        // TODO 错误文案信息抽取到前端字典里
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(500);
                    }
                });
            }

            // 已经登录
            // 计时交互
            var startTime = params['startTime']; //  = params.startTime; params是一个object对象
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];

            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result.data) {//如果取到了数据
                    var nowTime = result['data']; //  可以换成 = result.data; result是一个object
                    console.log("nowTimeTest:"+nowTime);
                    //时间判断 计时交互
                    seckill.countdown(seckillId,nowTime,startTime,endTime);
                }else {
                    console.log('result:'+result);
                    alert('result:'+result);
                }
            });


        }
    }
}