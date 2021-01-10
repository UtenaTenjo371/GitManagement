## 功能描述
实现了简单的暂存区，以支持git add功能，该功能可以指定添加根目录下的文件/文件夹，若对象为文件夹，该文件夹下的所有文件都会被添加到暂存区
## 设计思路
* 暂存区使用.mygit/index文件实现的
* 目前仅支持两种文件状态unstage、staged，以及unstage->staged的转换
* index文件里记录的是add的文件/文件夹的hashkey
* 需要更新
    * 创建tree object时，增加了对是否在暂存区中的判断
    * checkout分支和reset时，增加了对是否在暂存区中的判断
