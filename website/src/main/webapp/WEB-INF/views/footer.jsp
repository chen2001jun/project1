<div class="footer pd">
    <div class="container clearfix">
        <div class="logo"></div>
        <div class="footer-info">
            <ul class="footer-nav clearfix">
                <li><a class="linkcolor linkline" href="#">商务合作</a></li>
                <li><a class="linkcolor linkline" href="#">意见反馈</a></li>
                <li><a class="linkcolor linkline" href="#">关于我们</a></li>
                <li><a class="linkcolor linkline" href="#">版权声明</a></li>
            </ul>
            <div class="footer-text">

            </div>
        </div>
        <img class="qr" src="${assets}/images/qr.jpg" width="430" height="430" alt=""/>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        $.get(ctx + '/user/score', function (data) {
            if (data) {
                $('.score').html(data['totalScore']);
            }
        });
    });
</script>
</body>
</html>