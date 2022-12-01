import config from '@/config/index.js'

//全局函数 ，挂载到Vue实例上
export default function install(Vue) {
    //  加载缩略图    
    Vue.prototype.downloadImgMin = function (row) {
        let fileUrl = row.fileUrl;
        if (fileUrl) {
            let index = fileUrl.lastIndexOf(".");
            fileUrl = config.staticContext + fileUrl.substr(0, index) + "_min" + fileUrl.substr(index);
            // fileUrl = fileUrl.substr(0, index) + "_min" + fileUrl.substr(index);
        }
        return fileUrl;
    };

}