package com.edu.service;
/**
 * 이 클래스는 DAO메서드를 호출하는 기능을 합니다.
 * @author mooon
 *
 */

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.edu.dao.IF_BoardDAO;
import com.edu.vo.AttachVO;
import com.edu.vo.BoardVO;
import com.edu.vo.PageVO;

@Service   // 붙이면 스프링 빈으로 등록이 됨
public class BoardServiceImpl implements IF_BoardService {
	@Inject
	private IF_BoardDAO boardDAO;
	@Override
	public List<AttachVO> readAttach(Integer bno) throws Exception {
		// TODO 첨부파일list형으로 조회DAO호출
		return boardDAO.readAttach(bno);
	}

	@Override
	public int countBoard(PageVO pageVO) throws Exception {
		// TODO 페이징처리시 pageVO의 totalCount변수에 사용될 값을 리턴값으로받음.
		return boardDAO.countBoard(pageVO);
	}

	@Override
	public void deleteBoard(int bno) throws Exception {
		// TODO 게시물 삭제할떄,여기서는 3개의 메서드가 실행(댓글+첨부파일삭제+게물삭제)
		//트랜잭선이 필요한순간,작업순서:1.첨부파일삭제 2.게시물삭제 |시에러o삭제가안됨
		//위와 같은 상황을 방지하는 목적의 기능으로@Transantional 에노테니션사용합니다.
		//*특이사항: 첨부파일은 DB만 삭재해서해결x +실제 업로드된 파일을 삭제가 필요.-나중에추가
		
		boardDAO.deleteAttachAll(bno);
		boardDAO.deleteBoard(bno);
		//*댓글삭제는 나중에
	}

	@Override
	public void updateBoard(BoardVO boardVO) throws Exception {
		// TODO 첨부파일이 있으면 updateAttach -> 게시물 업데이트updateBpard
		boardDAO.updateBoard(boardVO);	
		//첨부파일 DB처리 (아래)
		int bno =boardVO.getBno();
		String[] save_file_names =boardVO.getSave_file_names();
		String[] real_file_names =boardVO.getReal_file_names();
		if(save_file_names==null) {return;}//조건이 맞지 않으면 빠져나감.이후 실행안함,
		AttachVO attachVO =new AttachVO();
		int index=0;
		String real_file_name ="";
		for(String save_file_name:save_file_names) {
			real_file_name = real_file_names[index];
			attachVO.setBno(bno);
			attachVO.setSave_file_name(save_file_name);
			attachVO.setReal_file_name(real_file_name);
			boardDAO.updateAttach(attachVO);
			index=index+1;//index++
		}
	}

	@Override
	public BoardVO readBoard(int bno) throws Exception {
		// TODO 게시물 상세보기시  실행순서readBoadr ->updateViewCount 2개의 메서드가 필요
		BoardVO boardVO =boardDAO.readBoard(bno);
		boardDAO.updateViewCount(bno);
		return boardVO;
	}

	@Override
	public void insertBoard(BoardVO boardVO) throws Exception {
		// TODO 부모 게시물 insertBoard실행->자식 첨부파일 있으면 첨부파일 insertAttach
		//게시물 등록+반환값으로 bno를 추가
		int bno = boardDAO.insertBoard(boardVO);
		//첨부파일이 1개 이상일떄 가정해서 처리
		String[] save_file_names=boardVO.getSave_file_names();//변수 만듬,폴더에 저장용 파일명들
		String[] real_file_names=boardVO.getReal_file_names();// UI용 배열 파일명들
		if(save_file_names ==null) {return;}//리턴이 발생되면 ,이후 실행안됨
		//첨부파일이 null이 아닐떄 아래가 진행됨
		int index=0;
		String real_file_name="";//UI용 1개 파일명
		AttachVO attachVO=new AttachVO();
		for(String save_file_name:save_file_names) {//첨부파일 개수만큼반복진행
			real_file_name =real_file_names[index];
			attachVO.setBno(bno);
			attachVO.setReal_file_name(real_file_name);
			attachVO.setSave_file_name(save_file_name);
			boardDAO.insertAttach(null); 
			index++;
		}
	}

	@Override
	public List<BoardVO> selectBoard(PageVO pageVO) throws Exception {
		// TODO DAO 1개만 호출하면됨
		return boardDAO.selectBoard(pageVO);
	}

}
