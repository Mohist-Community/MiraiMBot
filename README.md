# MiraiMBot
> 这是一个基于 [Mirai](https://github.com/mamoe/mirai) 的QQ机器人框架
>
> 强烈建议您在看完 [mirai的开发文档](https://github.com/mamoe/mirai/blob/dev/docs/README.md) 之后再来看这个文档
>
> 首先:
> * 您需要有一定的的java开发能力
> * 您需要会使用gradle
> * 您需要会使用IDEA
> * 您需要会使用 [mirai](https://github.com/mamoe/mirai) 进行开发
> * 如果你有什么问题，可以 [加入QQ群](https://qm.qq.com/cgi-bin/qm/qr?k=ZXJuf2VSDk5tzZe5DkZXUcdkkawCDxqv&jump_from=webapi) 进行交流

## 开始
> 1. 将这个仓库clone下来
> 2. 导入IDEA
> 3. 然后开始开发

如果你想进行MiraiMBot插件的开发,请看 [这里](https://github.com/lliioollcn/MiraiMBotExamplePlugin)

## 运行
 首先搞到不知道哪里来的构建好的MiraiMBot(或者自己clone构建)
 <br>
 然后运行一下,他会生成config.yml
 <br>
 他看起来像这样:
 ```yaml
# 登录设置
login:
  # 要登录的账号
  account: 0
  # 要登录账号的密码
  password: ''
# 指令设置
command:
  # 是否开启默认指令系统(true为开false为关)
  enable: false
  # 指令前缀
  prefix: '#'
  # 未知指令消息
  unknown: 未知指令。请使用"#help"来获得帮助
# 权限设置
permission:
  # 是否开启默认权限系统(true为开false为关)
  enable: false
log:
  # 网络日志开关(true为开false为关)
  network: true
  # mirai日志开关(true为开false为关)
  bot: true
# mirai协议(手机为ANDROID_PHONE 平板为ANDROID_PAD 手表为ANDROID_WATCH)
protocol: ANDROID_PHONE
```
将其中的account和password填写好后,先在本地运行一下生成device.json并且通过验证码之后再连同device.json一起上传到服务器
然后就可以愉快地进行开发啦~