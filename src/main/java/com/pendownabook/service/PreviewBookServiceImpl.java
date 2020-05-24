package com.pendownabook.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pendownabook.entities.Genre;
import com.pendownabook.entities.PreviewBook;
import com.pendownabook.entities.Publisher;
import com.pendownabook.entities.ReviewStatus;
import com.pendownabook.entities.Reviews;
import com.pendownabook.entities.User;
import com.pendownabook.repositories.GenreRepository;
import com.pendownabook.repositories.PreviewBookRepository;
import com.pendownabook.repositories.ReviewRepository;
import com.pendownabook.repositories.UserRepository;

@Service
public class PreviewBookServiceImpl implements PreviewBookService {
	@Autowired
	private PreviewBookRepository previewBookRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private GenreRepository genreRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Override
	public List<PreviewBook> getAll() {
		return previewBookRepository.findAll();
	}
	
	@Override
	public List<PreviewBook> getPreviewBook(String email){
		return previewBookRepository.findByUser(userRepository.findByEmail(email));
	}

	@Override
	public PreviewBook savePreviewBook(String email, Long genreId, String previewBookTitle, String previewBookPath) throws ParseException {
		User user = userRepository.findByEmail(email);
		Genre genre = genreRepository.findById(genreId).get();
		
		PreviewBook previewBook = new PreviewBook();
		previewBook.setGenre(genre);
		previewBook.setUser(user);
		previewBook.setTitle(previewBookTitle);
		previewBook.setPdfPath(previewBookPath);
		
		LocalDateTime now = LocalDateTime.now();
		System.out.println(now);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		String formatDateTime = now.format(format); 
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = ft.parse(formatDateTime);		
		previewBook.setDateOfUpload(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		
		return previewBookRepository.save(previewBook);
	}

	@Override
	public List<Object> getReviewsForPublisher(String publisherEmail) {		
		return previewBookRepository.findPreviewBooksLeftJoinPublisher(publisherEmail);
	}

	@Override
	public void addPublisherReviewForPreviewBook(Long previewBookId, ReviewStatus reviewStatus, Publisher publisher) throws ParseException {
		Reviews review = new Reviews();
		review.setReviewStatus(reviewStatus);
		review.setPublisher(publisher);
		review.setPreviewBook(previewBookRepository.findById(previewBookId).get());
		
		LocalDateTime now = LocalDateTime.now();
		System.out.println(now);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		String formatDateTime = now.format(format); 
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = ft.parse(formatDateTime);
		review.setDateOfReview(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		
		reviewRepository.save(review);
	}

}