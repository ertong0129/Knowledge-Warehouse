# SDS

redis没有使用c语言的字符串，而是自己构建了一种名叫简单动态字符串（simple dynamic string, SDS）的数据结构。

该结构如下：

```
struct sdshdr {
    //buf数组中已使用字节的数量，有效字符串长度
    int len;
    
    //buf数组中未使用字节的数量，用于数据修改
    int free;
    
    //字节数组，用于保存字符串
    char buf[];
}
```



使用SDS的意义：

1.O(1)获取字符串长度

2.杜绝缓冲区溢出。c字符串不记录自身长度，所以当一个字符串需要在最后添加字节时，可能会修改到未被分配到的空间的数据。

3.减少修改时内存需要重新分配的次数。SDS使用空间预分配策略。

如果对SDS字符串进行修改后，SDS的有效字符串长度（len）小于1MB，那么程序分配和len属性同样大小的未使用空间。如len为13，则buf的大小为13(len)+13(free)+1('\0')=27字节。

如果对SDS进行修改后，SDS的有效字符串长度（len）大于等于1MB，那么程序会分配1MB未使用空间。如len为30MB，则buf数组实际长度为30MB(len)+1MB(free)+1B('\0')

当要缩短SDS保存的字符串时，程序并不立即使用内存充分配来回收缩短后多出来的字节，而是使用表头的free将这些字节记录起来，并等待将来使用。

4.二进制安全。c字符串中字符除了末尾，字符串里面不能包含空字符，导致c字符串只能保存文本数据，不能保存图片、音频、视频、压缩文件这样的二进制数据。通过使用SDS，redis可以保存任意格式的二进制数据。





字符串类型的内部编码有三种：
1、int：存储 8 个字节的长整型（long，2^63-1）。
2、embstr：代表 embstr 格式的 SDS（Simple Dynamic String 简单动态字符串），
存储小于 44 个字节的字符串，只分配一次内存空间（因为 Redis Object 和 SDS 是连续的）。
3、raw：存储大于 44 个字节的字符串（3.2 版本之前是 39 个字节），需要分配两次内存空间（分别为 Redis Object 和 SDS 分配空间）。

ps：如果一个对一个字符串使用`append key value`的命令，编码会直接从embstr变为raw。与raw相比，embstr的好处在于创建时少分配一次空间，删除时少释放一次空间。对于embstr来说，如果字符串的长度增加需要重新分配内存时，整个redisObject和sds都需要重新分配空间