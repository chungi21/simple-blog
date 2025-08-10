package com.example.simple_blog.domain.post

import com.example.simple_blog.domain.member.Member
import com.linecorp.kotlinjdsl.query.spec.ExpressionOrderSpec
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.querydsl.from.fetch
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import jakarta.persistence.criteria.JoinType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.support.PageableExecutionUtils

interface PostRepository : JpaRepository<Post, Long>, PostCustomRepository {
    // 네이티브 방식 (필요할 시 사용 가능)
    fun findByMember(member: Member, pageable: Pageable): Page<Post>
    fun deleteByMember_Id(memberId: Long): Long
}

interface PostCustomRepository {
    fun findPosts(pageable: Pageable): Page<Post>
    fun findPostsByMember(member: Member, pageable: Pageable): Page<Post>
}

class PostCustomRepositoryImpl(
    private val queryFactory: SpringDataQueryFactory
) : PostCustomRepository {

    // 전체 조회
    override fun findPosts(pageable: Pageable): Page<Post> {
        val results = queryFactory.listQuery<Post> {
            select(entity(Post::class))
            from(entity(Post::class))
            fetch(Post::member)
            limit(pageable.pageSize)
            offset(pageable.offset.toInt())
            orderBy(ExpressionOrderSpec(column(Post::id), false))
        }

        val countQuery = queryFactory.listQuery<Post> {
            select(entity(Post::class))
            from(entity(Post::class))
        }

        return PageableExecutionUtils.getPage(results, pageable) {
            countQuery.size.toLong()
        }
    }

    // 특정 멤버의 글만 조회
    override fun findPostsByMember(member: Member, pageable: Pageable): Page<Post> {
        val results = queryFactory.listQuery<Post> {
            select(entity(Post::class))
            from(entity(Post::class))
            fetch(Post::member, JoinType.INNER)
            where(column(Post::member).equal(member))
            limit(pageable.pageSize)
            offset(pageable.offset.toInt())
            orderBy(ExpressionOrderSpec(column(Post::id), false))
        }

        val countQuery = queryFactory.listQuery<Post> {
            select(entity(Post::class))
            from(entity(Post::class))
            where(column(Post::member).equal(member))
        }

        return PageableExecutionUtils.getPage(results, pageable) {
            countQuery.size.toLong()
        }
    }
}
