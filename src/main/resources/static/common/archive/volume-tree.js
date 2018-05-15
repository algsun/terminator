function zTreeOnClick(event, treeId, treeNode) {
    
}

$(function () {
    $.get("/eudemon/archiveVolume/find-volume-tree.json", function (volumeTreeData) {
        var setting = {
            callback: {
                onClick: zTreeOnClick
            }
        };
        var zTreeObj = $.fn.zTree.init($("#volumeTree"), setting, volumeTreeData);
    });
});