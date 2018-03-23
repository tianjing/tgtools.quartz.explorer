var grid = null;
$(function () {
    mini.parse();
    grid = mini.get("datagrid1");
    grid.setUrl("../quartz/list");
    gridreload();
});
function gridreload() {
    grid.load();
};
function add() {
    var url = "edit.html";
    window.open(url);
};
function edit() {
    var selected = grid.getSelected();
    var url = "edit.html?mode=update&id="+selected.id;
    window.open(url);
};

function remove() {
    var selected = grid.getSelected();
    var url = "../../../../quartz/explorer/manage/quartz/remove";
    var res = tgtools.net.ajaxData("POST", url, {"id":selected.id});
    gridreload();
    alert(res);
};

function change() {
    var selected = grid.getSelected();
    var cmd ="stop";
    if("0"==selected.jobStatus)
    {
        cmd="start";
    }
    var data={"id":selected.id,"cmd":cmd};
    var url = "../../../../quartz/explorer/manage/quartz/changeJobStatus";
    var res = tgtools.net.ajaxData("POST", url, data);
    gridreload();
    alert(res);
};

