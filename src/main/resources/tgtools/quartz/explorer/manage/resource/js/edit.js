var form = null;
var mode = null;
$(function () {
    mini.parse();
    form = new mini.Form("#form1");
    mode = tgtools.util.URL.getQueryString("mode");
    if ("update" == mode) {
        initData();
    }
});

function initData() {
    var id = tgtools.util.URL.getQueryString("id");
    var url = "../../../../quartz/explorer/manage/quartz/get";
    var data = tgtools.net.ajaxData("GET", url, {"id":id});
    form.setData(data);
};

function submitForm() {
    form.validate();
    if (form.isValid() == false) return;

    var data = form.getData();
    if ("update" == mode) {
        updateData(data);
        return;
    }
    saveData(data);
};

function saveData(data) {
    var url = "../../../../quartz/explorer/manage/quartz/save";
    var res = tgtools.net.ajaxData("POST", url, data);
    alert(res);
};

function updateData(data) {
    var url = "../../../../quartz/explorer/manage/quartz/update";
    var res = tgtools.net.ajaxData("POST", url, data);
    alert(res);
};

function closeForm() {
    window.close();
};
function openCron()
{
    var url = "cron/cron.html";
    window.open(url);
};
function onCronValidation(e) {
    if (e.isValid) {
        var res=myCheckCron(e.value);
        if (res != true) {
            e.errorText = res;
            e.isValid = false;
        }
    }
};