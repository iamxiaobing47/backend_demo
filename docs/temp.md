我希望你帮我重构Message


我希望前后端交互是基于MessageCode和MessageArgs，而不是具体的Message
但现在Message设计的并不是很好

1.添加新的Code困难
2. 没有分类

我希望你将它拆分成几个文件，每个文件包含一类MessageCode
Message要区分几个类别

W -warning
N- notification
M - message
E - error
把类别编码到CODE里，例如W001
