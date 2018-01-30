$(function () {
    laydate.render({
        elem: '#searchBeginDate'
        ,type: 'datetime'
    });

    laydate.render({
        elem: '#searchEndDate'
        ,type: 'datetime'
    });
    var chartContainer = document.getElementById('exampleTable'),
        myChart = echarts.init(chartContainer);
    initChart();

    $("#search").click(function(){
        initChart();
    });



    function initChart(){

        $.post("/report/member/registerReport",
            {
                beginDate:$("#searchBeginDate").val(),
                endDate:$("#searchEndDate").val()
            },
            function (data) {
                if (data.code == '200') {
                    dailyReport(myChart, data.data);
                }
            });

    }

    function dailyReport(myChart, data) {


        var option = {
            title : {
                text: '用户统计表',
                subtext: '日报表',
                show:false
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['注册用户个数','实名用户个数',"绑卡用户个数"]
            },
            //右上角工具条
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    data : data.xaxis
                }
            ],
            yAxis : [
                {
                    type : 'value',
                    axisLabel : {
                        formatter: '{value} 人'
                    }
                }
            ],
            series : [
                {
                    name:'注册用户个数',
                    type:'line',
                    data:data.registerCountList/*,
                    markPoint : {
                        data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                        ]
                    },
                    markLine : {
                        data : [
                            {type : 'average', name: '平均值'}
                        ]
                    }*/
                },
                {
                    name:'实名用户个数',
                    type:'line',
                    data:data.realNameCountList/*,
                    markPoint : {
                        data : [
//                        {name : '周最低', value : -2, xAxis: 1, yAxis: -1.5}
                            {type : 'min', name: '周最低'}
                        ]
                    },
                    markLine : {
                        data : [
                            {type : 'average', name : '平均值'}
                        ]
                    }*/
                },
                {
                    name:'绑卡用户个数',
                    type:'line',
                    data:data.bindCardCountList/*,
                    markPoint : {
                        data : [
//                        {name : '周最低', value : -2, xAxis: 1, yAxis: -1.5}
                            {type : 'min', name: '周最低'}
                        ]
                    },
                    markLine : {
                        data : [
                            {type : 'average', name : '平均值'}
                        ]
                    }*/
                }
            ]
        };

        // 为echarts对象加载数据
        myChart.clear();
        myChart.setOption(option);


    }
});


