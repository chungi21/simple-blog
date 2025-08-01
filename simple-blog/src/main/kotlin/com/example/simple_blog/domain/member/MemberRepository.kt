package com.example.simple_blog.domain.member

import com.linecorp.kotlinjdsl.query.spec.ExpressionOrderSpec
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import jakarta.persistence.NoResultException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.support.PageableExecutionUtils

interface MemberRepository : JpaRepository<Member, Long>, MemberCustomRepository {

}

interface MemberCustomRepository {
    fun findMembers(pageable: Pageable): Page<Member>
    fun findMemberByEmail(email: String): Member
    fun findMemberByEmailOrNull(email: String): Member?
    fun findMemberByNicknameOrNull(nickname: String): Member?
}

class MemberCustomRepositoryImpl(
    private val queryFactory : SpringDataQueryFactory
) : MemberCustomRepository {

    override fun findMembers(pageable: Pageable) : Page<Member> {
        val results =  queryFactory.listQuery<Member>{
            select(entity(Member::class))
            from(entity(Member::class))
            limit(pageable.pageSize)
            offset(pageable.offset.toInt())
            orderBy(ExpressionOrderSpec(column(Member::id), false))
        }

        val countQuery = queryFactory.listQuery<Member> {
            select(entity(Member::class))
            from(entity(Member::class))
        }

        return  PageableExecutionUtils.getPage(results, pageable){
            countQuery.size.toLong()
        }

    }

    override fun findMemberByEmail(email: String) : Member{
        return queryFactory.singleQuery {
            select(entity(Member::class))
            from(entity(Member::class))
            where(
                column(Member::email).equal(email)
            )
        }
    }

    override fun findMemberByEmailOrNull(email: String): Member? {
        return try {
            queryFactory.singleQuery {
                select(entity(Member::class))
                from(entity(Member::class))
                where(column(Member::email).equal(email))
            }
        } catch (e: NoResultException) {
            null
        }
    }

    override fun findMemberByNicknameOrNull(nickname: String): Member? {
        return try {
            queryFactory.singleQuery {
                select(entity(Member::class))
                from(entity(Member::class))
                where(column(Member::nickname).equal(nickname))
            }
        } catch (e: NoResultException) {
            null
        }
    }

}
