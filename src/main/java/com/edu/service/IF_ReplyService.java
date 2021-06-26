package com.edu.service;

import java.util.List;

import com.edu.vo.PageVO;
import com.edu.vo.ReplyVO;

/**
 * 이 인테페이스는 댓글 DAO를 이용해서 댓글 CRUD를 구현하는 서비스
 * @author 뱃살 현문
 *
 */

public interface IF_ReplyService {
	
	public List<ReplyVO> selectReply(PageVO pageVO)throws Exception;
	public int countReply(Integer bno)throws Exception;
	public void insertReply(ReplyVO replyVO)throws Exception;//replyVO는 jsp폼에서
	public void updateReply(ReplyVO replyVO)throws Exception;
	public void deleteReply(ReplyVO replyVO)throws Exception;

}
