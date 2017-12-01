package fr.cephee.unilille.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fr.cephee.unilille.model.Member;

public class MemberRowMapper implements RowMapper
{
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		Member member = new Member();
		member.setId(rs.getInt("student_id"));
		member.setFirstname(rs.getString("firstname"));
		member.setLastname(rs.getString("lastname"));
		member.setBirth(rs.getDate("birth"));
		return member;
	}

}
