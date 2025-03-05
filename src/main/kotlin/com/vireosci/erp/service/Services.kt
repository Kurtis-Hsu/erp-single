package com.vireosci.erp.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisService @Autowired constructor(private val redisTemplate: RedisTemplate<String, Any>)
{
    /**
     * 向 Redis 添加 kv 数据
     */
    fun setVal(key: String, value: Any) = redisTemplate.opsForValue().set(key, value)

    /**
     * 根据 key 获取 Redis 中的 value
     */
    fun getVal(key: String): Any? = redisTemplate.opsForValue().get(key)

    /**
     * 根据 key 删除 Redis 中的 value
     */
    fun rem(key: String): Boolean? = redisTemplate.delete(key)
}