# tmux终端管理

命令行的典型使用方式是，打开一个终端窗口，在里面输入命令。

Tmux 就是会话与窗口的"解绑"工具，将它们彻底分离。

（1）它允许在单个窗口中，同时访问多个会话。这对于同时运行多个命令行程序很有用。

（2） 它可以让新窗口"接入"已经存在的会话。

（3）它允许每个会话有多个连接窗口，因此可以多人实时共享会话。

（4）它还支持窗口任意的垂直和水平拆分。



## 安装

```bash
apt-get install tmux
```



## 启动与退出

```bash
tmux new -s <session-name>
```

```bash
exit
```



## 快捷键

tmux 窗口有大量的快捷键。所有快捷键都要通过前缀键唤起。默认的前缀键是`Ctrl+b`，即先按下`Ctrl+b`，快捷键才会生效。

举例来说，帮助命令的快捷键是`Ctrl+b ?`



## 常用命令

1.分离会话

`Ctrl+b d`或`tmux detach`



2.查看所有会话

`tmux ls`

`Ctrl+b s`



3.接入会话

`tmux attach -t <session-name>`

`tmux attach -t <session-id>`



4.杀死会话

`tmux kill-session -t <session-name>` 

`tmux kill-session -t <session-id>`



5.切换会话

`tmux switch -t <session-name>`

`tmux switch -t <session-id>`



6.重命名会话

`tmux rename-session -t <session-id> <new-name>`

`Ctrl+b $`



7.划分窗格

划分上下两个窗格

```bash
tmux split-window
```

`Ctrl+b`

划分左右两个窗格

```bash
tmux split-window -h
```

`Ctrl+b %`

8.移动光标

```bash
# 光标切换到上方窗格
tmux select-pane -U

# 光标切换到下方窗格
tmux select-pane -D

# 光标切换到左边窗格
tmux select-pane -L

# 光标切换到右边窗格
tmux select-pane -R
```

`Ctrl+b <arrow key>`

`<arrow key>`是指向要切换到的窗格的方向键，比如切换到下方窗格，就按方向键`↓`。



9.交换窗格位置

```bash
# 当前窗格上移
tmux swap-pane -U

# 当前窗格下移
tmux swap-pane -D
```



10.关闭当前窗格

`Ctrl+b x`



11.当前窗格全屏显示（再使用一次会变回原来大小）

`Ctrl+b z`



## 参考链接

http://louiszhai.github.io/2017/09/30/tmux/

https://www.ruanyifeng.com/blog/2019/10/tmux.html