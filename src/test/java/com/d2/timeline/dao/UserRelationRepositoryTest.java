package com.d2.timeline.dao;

import com.d2.timeline.domain.dao.UserRelationRepository;
import com.d2.timeline.domain.vo.Member;
import com.d2.timeline.domain.vo.UserRelation;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRelationRepositoryTest extends RepositoryTest{
    @Autowired
    private UserRelationRepository userRelationRepo;


    private UserRelation userRelation;
    private Member master;
    private Member slave;

//    @Before
//    public void setUp() throws Exception {
//        final String value = "cheese10yun@gmail.com";
//        email = EmailBuilder.build(value);
//        final Name name = NameBuilder.build();
//        saveMember = memberRepository.save(MemberBuilder.build(email, name));
//    }
//
//    ...
//    @Test
//    public void existsByEmail_존재하는경우_true() {
//        final boolean existsByEmail = memberRepository.existsByEmail(email);
//        assertThat(existsByEmail).isTrue();
//    }
//
//    @Test
//    public void existsByEmail_존재하지않은_경우_false() {
//        final boolean existsByEmail = memberRepository.existsByEmail(Email.of("ehdgoanfrhkqortntksdls@asd.com"));
//        assertThat(existsByEmail).isFalse();
//    }
}
