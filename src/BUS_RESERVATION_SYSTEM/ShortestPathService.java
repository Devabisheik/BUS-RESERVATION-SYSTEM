package BUS_RESERVATION_SYSTEM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.ortools.Loader;
import com.google.ortools.graph.MinCostFlow;
import com.google.ortools.graph.MinCostFlowBase;

public class ShortestPathService {

	/* ===================== ROAD MODEL ===================== */
	static class Road {
		int from;
		int to;
		int distance;

		Road(int from, int to, int distance) {
			this.from = from;
			this.to = to;
			this.distance = distance;
		}
	}

	/* ===================== DISTRICT MAP ===================== */
	static class DistrictMap {

		private static final String[] DISTRICTS = { "Ariyalur", // 0
				"Chengalpattu", // 1
				"Chennai", // 2
				"Coimbatore", // 3
				"Cuddalore", // 4
				"Dharmapuri", // 5
				"Dindigul", // 6
				"Erode", // 7
				"Kallakurichi", // 8
				"Kanchipuram", // 9
				"Kanyakumari", // 10
				"Karur", // 11
				"Krishnagiri", // 12
				"Madurai", // 13
				"Mayiladuthurai", // 14
				"Nagapattinam", // 15
				"Namakkal", // 16
				"Nilgiris", // 17
				"Perambalur", // 18
				"Pudukkottai", // 19
				"Ramanathapuram", // 20
				"Ranipet", // 21
				"Salem", // 22
				"Sivagangai", // 23
				"Tenkasi", // 24
				"Thanjavur", // 25
				"Theni", // 26
				"Thoothukudi", // 27
				"Tiruchirappalli", // 28
				"Tirunelveli", // 29
				"Tirupattur", // 30
				"Tiruppur", // 31
				"Tiruvallur", // 32
				"Tiruvannamalai", // 33
				"Tiruvarur", // 34
				"Vellore", // 35
				"Viluppuram", // 36
				"Virudhunagar" // 37
		};

		private static final Map<String, Integer> nameToId = new HashMap<>();
		private static final Map<Integer, String> idToName = new HashMap<>();

		static {
			for (int i = 0; i < DISTRICTS.length; i++) {
				nameToId.put(DISTRICTS[i].toLowerCase(), i);
				idToName.put(i, DISTRICTS[i]);
			}
		}

		static int getId(String name) {
			return nameToId.get(name.toLowerCase());
		}

		static String getName(int id) {
			return idToName.get(id);
		}
	}

	/* ===================== RESULT WRAPPER ===================== */
	static class Result {
		List<String> path;
		long distance;

		Result(List<String> path, long distance) {
			this.path = path;
			this.distance = distance;
		}
	}

	/* ===================== ROAD NETWORK ===================== */
	static List<Road> roads = List.of(
			// ===== DISTANCES FROM CHENNAI (REFERENCE ONLY) =====
			// Chennai (2) Connections
			new Road(2, 0, 310), new Road(2, 1, 55), new Road(2, 2, 0), new Road(2, 3, 510), new Road(2, 4, 170),
			new Road(2, 5, 300), new Road(2, 6, 430), new Road(2, 7, 400), new Road(2, 8, 250), new Road(2, 9, 75),
			new Road(2, 10, 705), new Road(2, 11, 395), new Road(2, 12, 260), new Road(2, 13, 460),
			new Road(2, 14, 280), new Road(2, 15, 300), new Road(2, 16, 360), new Road(2, 17, 560),
			new Road(2, 18, 270), new Road(2, 19, 390), new Road(2, 20, 560), new Road(2, 21, 110),
			new Road(2, 22, 345), new Road(2, 23, 440), new Road(2, 24, 620), new Road(2, 25, 345),
			new Road(2, 26, 500), new Road(2, 27, 600), new Road(2, 28, 330), new Road(2, 29, 620),
			new Road(2, 30, 220), new Road(2, 31, 460), new Road(2, 32, 45), new Road(2, 33, 195), new Road(2, 34, 310),
			new Road(2, 35, 140), new Road(2, 36, 165), new Road(2, 37, 510),

			// Ariyalur (0) Connections
			new Road(0, 0, 0), new Road(0, 1, 260), new Road(0, 2, 310), new Road(0, 3, 235), new Road(0, 4, 105),
			new Road(0, 5, 210), new Road(0, 6, 175), new Road(0, 7, 180), new Road(0, 8, 95), new Road(0, 9, 245),
			new Road(0, 10, 450), new Road(0, 11, 130), new Road(0, 12, 240), new Road(0, 13, 215), new Road(0, 14, 85),
			new Road(0, 15, 125), new Road(0, 16, 115), new Road(0, 17, 320), new Road(0, 18, 45), new Road(0, 19, 130),
			new Road(0, 20, 245), new Road(0, 21, 250), new Road(0, 22, 145), new Road(0, 23, 190),
			new Road(0, 24, 375), new Road(0, 25, 45), new Road(0, 26, 265), new Road(0, 27, 360), new Road(0, 28, 70),
			new Road(0, 29, 370), new Road(0, 30, 230), new Road(0, 31, 195), new Road(0, 32, 330),
			new Road(0, 33, 175), new Road(0, 34, 110), new Road(0, 35, 240), new Road(0, 36, 145),
			new Road(0, 37, 265),

			// Chengalpattu (1) Connections
			new Road(1, 0, 260), new Road(1, 1, 0), new Road(1, 2, 55), new Road(1, 3, 455), new Road(1, 4, 120),
			new Road(1, 5, 250), new Road(1, 6, 380), new Road(1, 7, 345), new Road(1, 8, 200), new Road(1, 9, 35),
			new Road(1, 10, 650), new Road(1, 11, 340), new Road(1, 12, 210), new Road(1, 13, 410),
			new Road(1, 14, 230), new Road(1, 15, 250), new Road(1, 16, 310), new Road(1, 17, 505),
			new Road(1, 18, 220), new Road(1, 19, 340), new Road(1, 20, 510), new Road(1, 21, 85), new Road(1, 22, 295),
			new Road(1, 23, 390), new Road(1, 24, 570), new Road(1, 25, 295), new Road(1, 26, 450),
			new Road(1, 27, 550), new Road(1, 28, 280), new Road(1, 29, 570), new Road(1, 30, 175),
			new Road(1, 31, 410), new Road(1, 32, 80), new Road(1, 33, 145), new Road(1, 34, 260), new Road(1, 35, 110),
			new Road(1, 36, 110), new Road(1, 37, 460),

			new Road(3, 0, 235), new Road(3, 1, 455), new Road(3, 2, 510), new Road(3, 3, 0), new Road(3, 4, 340),
			new Road(3, 5, 195), new Road(3, 6, 155), new Road(3, 7, 95), new Road(3, 8, 260), new Road(3, 9, 440),
			new Road(3, 10, 440), new Road(3, 11, 130), new Road(3, 12, 250), new Road(3, 13, 215),
			new Road(3, 14, 320), new Road(3, 15, 345), new Road(3, 16, 165), new Road(3, 17, 85), new Road(3, 18, 205),
			new Road(3, 19, 230), new Road(3, 20, 330), new Road(3, 21, 400), new Road(3, 22, 165),
			new Road(3, 23, 265), new Road(3, 24, 345), new Road(3, 25, 275), new Road(3, 26, 185),
			new Road(3, 27, 360), new Road(3, 28, 205), new Road(3, 29, 355), new Road(3, 30, 280), new Road(3, 31, 55),
			new Road(3, 32, 490), new Road(3, 33, 335), new Road(3, 34, 315), new Road(3, 35, 380),
			new Road(3, 36, 350), new Road(3, 37, 260),

			new Road(4, 0, 105), new Road(4, 1, 120), new Road(4, 2, 170), new Road(4, 3, 340), new Road(4, 4, 0),
			new Road(4, 5, 235), new Road(4, 6, 275), new Road(4, 7, 280), new Road(4, 8, 110), new Road(4, 9, 135),
			new Road(4, 10, 560), new Road(4, 11, 230), new Road(4, 12, 230), new Road(4, 13, 320), new Road(4, 14, 85),
			new Road(4, 15, 115), new Road(4, 16, 215), new Road(4, 17, 425), new Road(4, 18, 130),
			new Road(4, 19, 235), new Road(4, 20, 350), new Road(4, 21, 165), new Road(4, 22, 200),
			new Road(4, 23, 300), new Road(4, 24, 480), new Road(4, 25, 160), new Road(4, 26, 370),
			new Road(4, 27, 465), new Road(4, 28, 200), new Road(4, 29, 475), new Road(4, 30, 210),
			new Road(4, 31, 300), new Road(4, 32, 215), new Road(4, 33, 115), new Road(4, 34, 135),
			new Road(4, 35, 175), new Road(4, 36, 45), new Road(4, 37, 375),

			new Road(5, 0, 210), new Road(5, 1, 250), new Road(5, 2, 300), new Road(5, 3, 195), new Road(5, 4, 235),
			new Road(5, 5, 0), new Road(5, 6, 220), new Road(5, 7, 120), new Road(5, 8, 175), new Road(5, 9, 235),
			new Road(5, 10, 560), new Road(5, 11, 160), new Road(5, 12, 50), new Road(5, 13, 275), new Road(5, 14, 300),
			new Road(5, 15, 325), new Road(5, 16, 110), new Road(5, 17, 260), new Road(5, 18, 170),
			new Road(5, 19, 260), new Road(5, 20, 395), new Road(5, 21, 200), new Road(5, 22, 65), new Road(5, 23, 310),
			new Road(5, 24, 460), new Road(5, 25, 240), new Road(5, 26, 305), new Road(5, 27, 430),
			new Road(5, 28, 205), new Road(5, 29, 440), new Road(5, 30, 105), new Road(5, 31, 160),
			new Road(5, 32, 285), new Road(5, 33, 145), new Road(5, 34, 310), new Road(5, 35, 185),
			new Road(5, 36, 190), new Road(5, 37, 335),

			new Road(6, 0, 175), new Road(6, 1, 380), new Road(6, 2, 430), new Road(6, 3, 155), new Road(6, 4, 275),
			new Road(6, 5, 220), new Road(6, 6, 0), new Road(6, 7, 145), new Road(6, 8, 225), new Road(6, 9, 390),
			new Road(6, 10, 305), new Road(6, 11, 75), new Road(6, 12, 270), new Road(6, 13, 65), new Road(6, 14, 250),
			new Road(6, 15, 265), new Road(6, 16, 105), new Road(6, 17, 240), new Road(6, 18, 140),
			new Road(6, 19, 135), new Road(6, 20, 175), new Road(6, 21, 355), new Road(6, 22, 160),
			new Road(6, 23, 110), new Road(6, 24, 220), new Road(6, 25, 170), new Road(6, 26, 75), new Road(6, 27, 215),
			new Road(6, 28, 100), new Road(6, 29, 215), new Road(6, 30, 290), new Road(6, 31, 105),
			new Road(6, 32, 450), new Road(6, 33, 295), new Road(6, 34, 225), new Road(6, 35, 330),
			new Road(6, 36, 280), new Road(6, 37, 115),

			new Road(7, 0, 180), new Road(7, 1, 345), new Road(7, 2, 400), new Road(7, 3, 95), new Road(7, 4, 280),
			new Road(7, 5, 120), new Road(7, 6, 145), new Road(7, 7, 0), new Road(7, 8, 175), new Road(7, 9, 345),
			new Road(7, 10, 440), new Road(7, 11, 65), new Road(7, 12, 175), new Road(7, 13, 205), new Road(7, 14, 260),
			new Road(7, 15, 290), new Road(7, 16, 55), new Road(7, 17, 150), new Road(7, 18, 155), new Road(7, 19, 195),
			new Road(7, 20, 315), new Road(7, 21, 305), new Road(7, 22, 65), new Road(7, 23, 240), new Road(7, 24, 345),
			new Road(7, 25, 215), new Road(7, 26, 200), new Road(7, 27, 355), new Road(7, 28, 145),
			new Road(7, 29, 350), new Road(7, 30, 195), new Road(7, 31, 55), new Road(7, 32, 400), new Road(7, 33, 245),
			new Road(7, 34, 260), new Road(7, 35, 290), new Road(7, 36, 245), new Road(7, 37, 245),

			new Road(8, 0, 95), new Road(8, 1, 200), new Road(8, 2, 250), new Road(8, 3, 260), new Road(8, 4, 110),
			new Road(8, 5, 175), new Road(8, 6, 225), new Road(8, 7, 175), new Road(8, 8, 0), new Road(8, 9, 180),
			new Road(8, 10, 500), new Road(8, 11, 165), new Road(8, 12, 185), new Road(8, 13, 275),
			new Road(8, 14, 190), new Road(8, 15, 215), new Road(8, 16, 115), new Road(8, 17, 345), new Road(8, 18, 75),
			new Road(8, 19, 210), new Road(8, 20, 340), new Road(8, 21, 185), new Road(8, 22, 105),
			new Road(8, 23, 260), new Road(8, 24, 435), new Road(8, 25, 165), new Road(8, 26, 320),
			new Road(8, 27, 430), new Road(8, 28, 140), new Road(8, 29, 435), new Road(8, 30, 155),
			new Road(8, 31, 220), new Road(8, 32, 270), new Road(8, 33, 100), new Road(8, 34, 210),
			new Road(8, 35, 175), new Road(8, 36, 75), new Road(8, 37, 325),

			new Road(9, 0, 245), new Road(9, 1, 35), new Road(9, 2, 75), new Road(9, 3, 440), new Road(9, 4, 135),
			new Road(9, 5, 235), new Road(9, 6, 390), new Road(9, 7, 345), new Road(9, 8, 180), new Road(9, 9, 0),
			new Road(9, 10, 660), new Road(9, 11, 345), new Road(9, 12, 190), new Road(9, 13, 415),
			new Road(9, 14, 250), new Road(9, 15, 275), new Road(9, 16, 310), new Road(9, 17, 500),
			new Road(9, 18, 225), new Road(9, 19, 345), new Road(9, 20, 520), new Road(9, 21, 55), new Road(9, 22, 280),
			new Road(9, 23, 410), new Road(9, 24, 580), new Road(9, 25, 305), new Road(9, 26, 455),
			new Road(9, 27, 560), new Road(9, 28, 285), new Road(9, 29, 580), new Road(9, 30, 155),
			new Road(9, 31, 395), new Road(9, 32, 65), new Road(9, 33, 115), new Road(9, 34, 280), new Road(9, 35, 75),
			new Road(9, 36, 115), new Road(9, 37, 470),

			new Road(10, 0, 450), new Road(10, 1, 650), new Road(10, 2, 705), new Road(10, 3, 440),
			new Road(10, 4, 560), new Road(10, 5, 560), new Road(10, 6, 305), new Road(10, 7, 440),
			new Road(10, 8, 500), new Road(10, 9, 660), new Road(10, 10, 0), new Road(10, 11, 380),
			new Road(10, 12, 605), new Road(10, 13, 245), new Road(10, 14, 530), new Road(10, 15, 500),
			new Road(10, 16, 410), new Road(10, 17, 520), new Road(10, 18, 430), new Road(10, 19, 375),
			new Road(10, 20, 250), new Road(10, 21, 680), new Road(10, 22, 465), new Road(10, 23, 315),
			new Road(10, 24, 160), new Road(10, 25, 435), new Road(10, 26, 310), new Road(10, 27, 125),
			new Road(10, 28, 385), new Road(10, 29, 85), new Road(10, 30, 595), new Road(10, 31, 445),
			new Road(10, 32, 725), new Road(10, 33, 585), new Road(10, 34, 495), new Road(10, 35, 655),
			new Road(10, 36, 545), new Road(10, 37, 195),

			new Road(11, 0, 130), new Road(11, 1, 340), new Road(11, 2, 395), new Road(11, 3, 130),
			new Road(11, 4, 230), new Road(11, 5, 160), new Road(11, 6, 75), new Road(11, 7, 65), new Road(11, 8, 165),
			new Road(11, 9, 345), new Road(11, 10, 380), new Road(11, 11, 0), new Road(11, 12, 210),
			new Road(11, 13, 140), new Road(11, 14, 210), new Road(11, 15, 230), new Road(11, 16, 35),
			new Road(11, 17, 215), new Road(11, 18, 110), new Road(11, 19, 115), new Road(11, 20, 245),
			new Road(11, 21, 340), new Road(11, 22, 95), new Road(11, 23, 165), new Road(11, 24, 295),
			new Road(11, 25, 145), new Road(11, 26, 150), new Road(11, 27, 285), new Road(11, 28, 80),
			new Road(11, 29, 290), new Road(11, 30, 245), new Road(11, 31, 85), new Road(11, 32, 410),
			new Road(11, 33, 260), new Road(11, 34, 190), new Road(11, 35, 310), new Road(11, 36, 240),
			new Road(11, 37, 190),

			new Road(12, 0, 240), new Road(12, 1, 210), new Road(12, 2, 260), new Road(12, 3, 250),
			new Road(12, 4, 230), new Road(12, 5, 50), new Road(12, 6, 270), new Road(12, 7, 175), new Road(12, 8, 185),
			new Road(12, 9, 190), new Road(12, 10, 605), new Road(12, 11, 210), new Road(12, 12, 0),
			new Road(12, 13, 335), new Road(12, 14, 340), new Road(12, 15, 365), new Road(12, 16, 160),
			new Road(12, 17, 310), new Road(12, 18, 220), new Road(12, 19, 310), new Road(12, 20, 445),
			new Road(12, 21, 150), new Road(12, 22, 115), new Road(12, 23, 365), new Road(12, 24, 515),
			new Road(12, 25, 295), new Road(12, 26, 360), new Road(12, 27, 485), new Road(12, 28, 260),
			new Road(12, 29, 495), new Road(12, 30, 55), new Road(12, 31, 215), new Road(12, 32, 235),
			new Road(12, 33, 125), new Road(12, 34, 350), new Road(12, 35, 125), new Road(12, 36, 200),
			new Road(12, 37, 390),

			new Road(13, 0, 215), new Road(13, 1, 410), new Road(13, 2, 460), new Road(13, 3, 215),
			new Road(13, 4, 320), new Road(13, 5, 275), new Road(13, 6, 65), new Road(13, 7, 205), new Road(13, 8, 275),
			new Road(13, 9, 415), new Road(13, 10, 245), new Road(13, 11, 140), new Road(13, 12, 335),
			new Road(13, 13, 0), new Road(13, 14, 285), new Road(13, 15, 260), new Road(13, 16, 170),
			new Road(13, 17, 300), new Road(13, 18, 200), new Road(13, 19, 110), new Road(13, 20, 115),
			new Road(13, 21, 420), new Road(13, 22, 225), new Road(13, 23, 45), new Road(13, 24, 160),
			new Road(13, 25, 195), new Road(13, 26, 75), new Road(13, 27, 155), new Road(13, 28, 135),
			new Road(13, 29, 160), new Road(13, 30, 360), new Road(13, 31, 185), new Road(13, 32, 500),
			new Road(13, 33, 335), new Road(13, 34, 220), new Road(13, 35, 410), new Road(13, 36, 315),
			new Road(13, 37, 55),

			new Road(14, 0, 85), new Road(14, 1, 230), new Road(14, 2, 280), new Road(14, 3, 320), new Road(14, 4, 85),
			new Road(14, 5, 300), new Road(14, 6, 250), new Road(14, 7, 260), new Road(14, 8, 190),
			new Road(14, 9, 250), new Road(14, 10, 530), new Road(14, 11, 210), new Road(14, 12, 340),
			new Road(14, 13, 285), new Road(14, 14, 0), new Road(14, 15, 65), new Road(14, 16, 205),
			new Road(14, 17, 400), new Road(14, 18, 125), new Road(14, 19, 165), new Road(14, 20, 290),
			new Road(14, 21, 280), new Road(14, 22, 235), new Road(14, 23, 230), new Road(14, 24, 445),
			new Road(14, 25, 75), new Road(14, 26, 350), new Road(14, 27, 395), new Road(14, 28, 135),
			new Road(14, 29, 440), new Road(14, 30, 315), new Road(14, 31, 275), new Road(14, 32, 330),
			new Road(14, 33, 225), new Road(14, 34, 40), new Road(14, 35, 300), new Road(14, 36, 160),
			new Road(14, 37, 340),

			new Road(15, 0, 125), new Road(15, 1, 250), new Road(15, 2, 300), new Road(15, 3, 345),
			new Road(15, 4, 115), new Road(15, 5, 325), new Road(15, 6, 265), new Road(15, 7, 290),
			new Road(15, 8, 215), new Road(15, 9, 275), new Road(15, 10, 500), new Road(15, 11, 230),
			new Road(15, 12, 365), new Road(15, 13, 260), new Road(15, 14, 65), new Road(15, 15, 0),
			new Road(15, 16, 225), new Road(15, 17, 430), new Road(15, 18, 150), new Road(15, 19, 145),
			new Road(15, 20, 240), new Road(15, 21, 305), new Road(15, 22, 260), new Road(15, 23, 205),
			new Road(15, 24, 410), new Road(15, 25, 85), new Road(15, 26, 335), new Road(15, 27, 360),
			new Road(15, 28, 145), new Road(15, 29, 410), new Road(15, 30, 340), new Road(15, 31, 305),
			new Road(15, 32, 355), new Road(15, 33, 250), new Road(15, 34, 25), new Road(15, 35, 325),
			new Road(15, 36, 185), new Road(15, 37, 315),

			new Road(16, 0, 115), new Road(16, 1, 310), new Road(16, 2, 360), new Road(16, 3, 165),
			new Road(16, 4, 215), new Road(16, 5, 110), new Road(16, 6, 105), new Road(16, 7, 55), new Road(16, 8, 115),
			new Road(16, 9, 310), new Road(16, 10, 410), new Road(16, 11, 35), new Road(16, 12, 160),
			new Road(16, 13, 170), new Road(16, 14, 205), new Road(16, 15, 225), new Road(16, 16, 0),
			new Road(16, 17, 245), new Road(16, 18, 75), new Road(16, 19, 145), new Road(16, 20, 275),
			new Road(16, 21, 310), new Road(16, 22, 55), new Road(16, 23, 195), new Road(16, 24, 325),
			new Road(16, 25, 155), new Road(16, 26, 180), new Road(16, 27, 315), new Road(16, 28, 85),
			new Road(16, 29, 320), new Road(16, 30, 215), new Road(16, 31, 115), new Road(16, 32, 375),
			new Road(16, 33, 210), new Road(16, 34, 200), new Road(16, 35, 280), new Road(16, 36, 205),
			new Road(16, 37, 220),

			new Road(17, 0, 320), new Road(17, 1, 505), new Road(17, 2, 560), new Road(17, 3, 85), new Road(17, 4, 425),
			new Road(17, 5, 260), new Road(17, 6, 240), new Road(17, 7, 150), new Road(17, 8, 345),
			new Road(17, 9, 500), new Road(17, 10, 520), new Road(17, 11, 215), new Road(17, 12, 310),
			new Road(17, 13, 300), new Road(17, 14, 400), new Road(17, 15, 430), new Road(17, 16, 245),
			new Road(17, 17, 0), new Road(17, 18, 290), new Road(17, 19, 315), new Road(17, 20, 415),
			new Road(17, 21, 485), new Road(17, 22, 245), new Road(17, 23, 350), new Road(17, 24, 430),
			new Road(17, 25, 360), new Road(17, 26, 270), new Road(17, 27, 445), new Road(17, 28, 290),
			new Road(17, 29, 440), new Road(17, 30, 365), new Road(17, 31, 140), new Road(17, 32, 550),
			new Road(17, 33, 420), new Road(17, 34, 400), new Road(17, 35, 465), new Road(17, 36, 435),
			new Road(17, 37, 345),

			new Road(18, 0, 45), new Road(18, 1, 220), new Road(18, 2, 270), new Road(18, 3, 205), new Road(18, 4, 130),
			new Road(18, 5, 170), new Road(18, 6, 140), new Road(18, 7, 155), new Road(18, 8, 75), new Road(18, 9, 225),
			new Road(18, 10, 430), new Road(18, 11, 110), new Road(18, 12, 220), new Road(18, 13, 200),
			new Road(18, 14, 125), new Road(18, 15, 150), new Road(18, 16, 75), new Road(18, 17, 290),
			new Road(18, 18, 0), new Road(18, 19, 115), new Road(18, 20, 265), new Road(18, 21, 230),
			new Road(18, 22, 110), new Road(18, 23, 185), new Road(18, 24, 360), new Road(18, 25, 100),
			new Road(18, 26, 250), new Road(18, 27, 345), new Road(18, 28, 55), new Road(18, 29, 355),
			new Road(18, 30, 210), new Road(18, 31, 175), new Road(18, 32, 305), new Road(18, 33, 165),
			new Road(18, 34, 135), new Road(18, 35, 220), new Road(18, 36, 120), new Road(18, 37, 250),

			new Road(19, 0, 130), new Road(19, 1, 340), new Road(19, 2, 390), new Road(19, 3, 230),
			new Road(19, 4, 235), new Road(19, 5, 260), new Road(19, 6, 135), new Road(19, 7, 195),
			new Road(19, 8, 210), new Road(19, 9, 345), new Road(19, 10, 375), new Road(19, 11, 115),
			new Road(19, 12, 310), new Road(19, 13, 110), new Road(19, 14, 165), new Road(19, 15, 145),
			new Road(19, 16, 145), new Road(19, 17, 315), new Road(19, 18, 115), new Road(19, 19, 0),
			new Road(19, 20, 150), new Road(19, 21, 375), new Road(19, 22, 200), new Road(19, 23, 60),
			new Road(19, 24, 270), new Road(19, 25, 60), new Road(19, 26, 185), new Road(19, 27, 265),
			new Road(19, 28, 55), new Road(19, 29, 270), new Road(19, 30, 315), new Road(19, 31, 215),
			new Road(19, 32, 435), new Road(19, 33, 285), new Road(19, 34, 125), new Road(19, 35, 365),
			new Road(19, 36, 255), new Road(19, 37, 165),

			new Road(20, 0, 245), new Road(20, 1, 510), new Road(20, 2, 560), new Road(20, 3, 330),
			new Road(20, 4, 350), new Road(20, 5, 395), new Road(20, 6, 175), new Road(20, 7, 315),
			new Road(20, 8, 340), new Road(20, 9, 520), new Road(20, 10, 250), new Road(20, 11, 245),
			new Road(20, 12, 445), new Road(20, 13, 115), new Road(20, 14, 290), new Road(20, 15, 240),
			new Road(20, 16, 275), new Road(20, 17, 415), new Road(20, 18, 265), new Road(20, 19, 150),
			new Road(20, 20, 0), new Road(20, 21, 530), new Road(20, 22, 330), new Road(20, 23, 85),
			new Road(20, 24, 220), new Road(20, 25, 190), new Road(20, 26, 190), new Road(20, 27, 125),
			new Road(20, 28, 200), new Road(20, 29, 185), new Road(20, 30, 470), new Road(20, 31, 300),
			new Road(20, 32, 605), new Road(20, 33, 415), new Road(20, 34, 205), new Road(20, 35, 520),
			new Road(20, 36, 400), new Road(20, 37, 120),

			new Road(21, 0, 250), new Road(21, 1, 85), new Road(21, 2, 110), new Road(21, 3, 400), new Road(21, 4, 165),
			new Road(21, 5, 200), new Road(21, 6, 355), new Road(21, 7, 305), new Road(21, 8, 185), new Road(21, 9, 55),
			new Road(21, 10, 680), new Road(21, 11, 340), new Road(21, 12, 150), new Road(21, 13, 420),
			new Road(21, 14, 280), new Road(21, 15, 305), new Road(21, 16, 310), new Road(21, 17, 485),
			new Road(21, 18, 230), new Road(21, 19, 375), new Road(21, 20, 530), new Road(21, 21, 0),
			new Road(21, 22, 245), new Road(21, 23, 435), new Road(21, 24, 580), new Road(21, 25, 330),
			new Road(21, 26, 445), new Road(21, 27, 585), new Road(21, 28, 290), new Road(21, 29, 600),
			new Road(21, 30, 115), new Road(21, 31, 360), new Road(21, 32, 90), new Road(21, 33, 110),
			new Road(21, 34, 305), new Road(21, 35, 25), new Road(21, 36, 140), new Road(21, 37, 480),

			new Road(22, 0, 145), new Road(22, 1, 295), new Road(22, 2, 345), new Road(22, 3, 165),
			new Road(22, 4, 200), new Road(22, 5, 65), new Road(22, 6, 160), new Road(22, 7, 65), new Road(22, 8, 105),
			new Road(22, 9, 280), new Road(22, 10, 465), new Road(22, 11, 95), new Road(22, 12, 115),
			new Road(22, 13, 225), new Road(22, 14, 235), new Road(22, 15, 260), new Road(22, 16, 55),
			new Road(22, 17, 245), new Road(22, 18, 110), new Road(22, 19, 200), new Road(22, 20, 330),
			new Road(22, 21, 245), new Road(22, 22, 0), new Road(22, 23, 250), new Road(22, 24, 380),
			new Road(22, 25, 195), new Road(22, 26, 235), new Road(22, 27, 370), new Road(22, 28, 140),
			new Road(22, 29, 375), new Road(22, 30, 170), new Road(22, 31, 120), new Road(22, 32, 360),
			new Road(22, 33, 155), new Road(22, 34, 235), new Road(22, 35, 220), new Road(22, 36, 175),
			new Road(22, 37, 280),

			new Road(23, 0, 190), new Road(23, 1, 390), new Road(23, 2, 440), new Road(23, 3, 265),
			new Road(23, 4, 300), new Road(23, 5, 310), new Road(23, 6, 110), new Road(23, 7, 240),
			new Road(23, 8, 260), new Road(23, 9, 410), new Road(23, 10, 315), new Road(23, 11, 165),
			new Road(23, 12, 365), new Road(23, 13, 45), new Road(23, 14, 230), new Road(23, 15, 205),
			new Road(23, 16, 195), new Road(23, 17, 350), new Road(23, 18, 185), new Road(23, 19, 60),
			new Road(23, 20, 85), new Road(23, 21, 435), new Road(23, 22, 250), new Road(23, 23, 0),
			new Road(23, 24, 210), new Road(23, 25, 115), new Road(23, 26, 120), new Road(23, 27, 175),
			new Road(23, 28, 105), new Road(23, 29, 180), new Road(23, 30, 385), new Road(23, 31, 230),
			new Road(23, 32, 480), new Road(23, 33, 340), new Road(23, 34, 180), new Road(23, 35, 420),
			new Road(23, 36, 310), new Road(23, 37, 85),

			new Road(24, 0, 375), new Road(24, 1, 570), new Road(24, 2, 620), new Road(24, 3, 345),
			new Road(24, 4, 480), new Road(24, 5, 460), new Road(24, 6, 220), new Road(24, 7, 345),
			new Road(24, 8, 435), new Road(24, 9, 580), new Road(24, 10, 160), new Road(24, 11, 295),
			new Road(24, 12, 515), new Road(24, 13, 160), new Road(24, 14, 445), new Road(24, 15, 410),
			new Road(24, 16, 325), new Road(24, 17, 430), new Road(24, 18, 360), new Road(24, 19, 270),
			new Road(24, 20, 220), new Road(24, 21, 580), new Road(24, 22, 380), new Road(24, 23, 210),
			new Road(24, 24, 0), new Road(24, 25, 335), new Road(24, 26, 175), new Road(24, 27, 145),
			new Road(24, 28, 290), new Road(24, 29, 55), new Road(24, 30, 500), new Road(24, 31, 350),
			new Road(24, 32, 635), new Road(24, 33, 505), new Road(24, 34, 405), new Road(24, 35, 570),
			new Road(24, 36, 465), new Road(24, 37, 105),

			new Road(25, 0, 45), new Road(25, 1, 295), new Road(25, 2, 345), new Road(25, 3, 275), new Road(25, 4, 160),
			new Road(25, 5, 240), new Road(25, 6, 170), new Road(25, 7, 215), new Road(25, 8, 165),
			new Road(25, 9, 305), new Road(25, 10, 435), new Road(25, 11, 145), new Road(25, 12, 295),
			new Road(25, 13, 195), new Road(25, 14, 75), new Road(25, 15, 85), new Road(25, 16, 155),
			new Road(25, 17, 360), new Road(25, 18, 100), new Road(25, 19, 60), new Road(25, 20, 190),
			new Road(25, 21, 330), new Road(25, 22, 195), new Road(25, 23, 115), new Road(25, 24, 335),
			new Road(25, 25, 0), new Road(25, 26, 265), new Road(25, 27, 330), new Road(25, 28, 55),
			new Road(25, 29, 330), new Road(25, 30, 290), new Road(25, 31, 260), new Road(25, 32, 400),
			new Road(25, 33, 235), new Road(25, 34, 40), new Road(25, 35, 315), new Road(25, 36, 200),
			new Road(25, 37, 250),

			new Road(26, 0, 265), new Road(26, 1, 450), new Road(26, 2, 500), new Road(26, 3, 185),
			new Road(26, 4, 370), new Road(26, 5, 305), new Road(26, 6, 75), new Road(26, 7, 200), new Road(26, 8, 320),
			new Road(26, 9, 455), new Road(26, 10, 310), new Road(26, 11, 150), new Road(26, 12, 360),
			new Road(26, 13, 75), new Road(26, 14, 350), new Road(26, 15, 335), new Road(26, 16, 180),
			new Road(26, 17, 270), new Road(26, 18, 250), new Road(26, 19, 185), new Road(26, 20, 190),
			new Road(26, 21, 445), new Road(26, 22, 235), new Road(26, 23, 120), new Road(26, 24, 175),
			new Road(26, 25, 265), new Road(26, 26, 0), new Road(26, 27, 225), new Road(26, 28, 175),
			new Road(26, 29, 230), new Road(26, 30, 400), new Road(26, 31, 160), new Road(26, 32, 535),
			new Road(26, 33, 385), new Road(26, 34, 305), new Road(26, 35, 450), new Road(26, 36, 360),
			new Road(26, 37, 105),

			new Road(27, 0, 360), new Road(27, 1, 550), new Road(27, 2, 600), new Road(27, 3, 360),
			new Road(27, 4, 465), new Road(27, 5, 430), new Road(27, 6, 215), new Road(27, 7, 355),
			new Road(27, 8, 430), new Road(27, 9, 560), new Road(27, 10, 125), new Road(27, 11, 285),
			new Road(27, 12, 485), new Road(27, 13, 155), new Road(27, 14, 395), new Road(27, 15, 360),
			new Road(27, 16, 315), new Road(27, 17, 445), new Road(27, 18, 345), new Road(27, 19, 265),
			new Road(27, 20, 125), new Road(27, 21, 585), new Road(27, 22, 370), new Road(27, 23, 175),
			new Road(27, 24, 145), new Road(27, 25, 330), new Road(27, 26, 225), new Road(27, 27, 0),
			new Road(27, 28, 280), new Road(27, 29, 55), new Road(27, 30, 520), new Road(27, 31, 355),
			new Road(27, 32, 630), new Road(27, 33, 495), new Road(27, 34, 345), new Road(27, 35, 580),
			new Road(27, 36, 455), new Road(27, 37, 100),

			new Road(28, 0, 70), new Road(28, 1, 280), new Road(28, 2, 330), new Road(28, 3, 205), new Road(28, 4, 200),
			new Road(28, 5, 205), new Road(28, 6, 100), new Road(28, 7, 145), new Road(28, 8, 140),
			new Road(28, 9, 285), new Road(28, 10, 385), new Road(28, 11, 80), new Road(28, 12, 260),
			new Road(28, 13, 135), new Road(28, 14, 135), new Road(28, 15, 145), new Road(28, 16, 85),
			new Road(28, 17, 290), new Road(28, 18, 55), new Road(28, 19, 55), new Road(28, 20, 200),
			new Road(28, 21, 290), new Road(28, 22, 140), new Road(28, 23, 105), new Road(28, 24, 290),
			new Road(28, 25, 55), new Road(28, 26, 175), new Road(28, 27, 280), new Road(28, 28, 0),
			new Road(28, 29, 290), new Road(28, 30, 255), new Road(28, 31, 190), new Road(28, 32, 355),
			new Road(28, 33, 215), new Road(28, 34, 110), new Road(28, 35, 270), new Road(28, 36, 160),
			new Road(28, 37, 190),

			new Road(29, 0, 370), new Road(29, 1, 570), new Road(29, 2, 620), new Road(29, 3, 355),
			new Road(29, 4, 475), new Road(29, 5, 440), new Road(29, 6, 215), new Road(29, 7, 350),
			new Road(29, 8, 435), new Road(29, 9, 580), new Road(29, 10, 85), new Road(29, 11, 290),
			new Road(29, 12, 495), new Road(29, 13, 160), new Road(29, 14, 440), new Road(29, 15, 410),
			new Road(29, 16, 320), new Road(29, 17, 440), new Road(29, 18, 355), new Road(29, 19, 270),
			new Road(29, 20, 185), new Road(29, 21, 600), new Road(29, 22, 375), new Road(29, 23, 180),
			new Road(29, 24, 55), new Road(29, 25, 330), new Road(29, 26, 230), new Road(29, 27, 55),
			new Road(29, 28, 290), new Road(29, 29, 0), new Road(29, 30, 520), new Road(29, 31, 350),
			new Road(29, 32, 645), new Road(29, 33, 510), new Road(29, 34, 405), new Road(29, 35, 595),
			new Road(29, 36, 465), new Road(29, 37, 105),

			new Road(30, 0, 230), new Road(30, 1, 175), new Road(30, 2, 220), new Road(30, 3, 280),
			new Road(30, 4, 210), new Road(30, 5, 105), new Road(30, 6, 290), new Road(30, 7, 195),
			new Road(30, 8, 155), new Road(30, 9, 155), new Road(30, 10, 595), new Road(30, 11, 245),
			new Road(30, 12, 55), new Road(30, 13, 360), new Road(30, 14, 315), new Road(30, 15, 340),
			new Road(30, 16, 215), new Road(30, 17, 365), new Road(30, 18, 210), new Road(30, 19, 315),
			new Road(30, 20, 470), new Road(30, 21, 115), new Road(30, 22, 170), new Road(30, 23, 385),
			new Road(30, 24, 500), new Road(30, 35, 290), new Road(30, 26, 400), new Road(30, 27, 520),
			new Road(30, 28, 255), new Road(30, 29, 520), new Road(30, 30, 0), new Road(30, 31, 245),
			new Road(30, 32, 210), new Road(30, 33, 95), new Road(30, 34, 335), new Road(30, 35, 90),
			new Road(30, 36, 175), new Road(30, 37, 415),

			new Road(31, 0, 195), new Road(31, 1, 410), new Road(31, 2, 460), new Road(31, 3, 55), new Road(31, 4, 300),
			new Road(31, 5, 160), new Road(31, 6, 105), new Road(31, 7, 55), new Road(31, 8, 220), new Road(31, 9, 395),
			new Road(31, 10, 445), new Road(31, 11, 85), new Road(31, 12, 215), new Road(31, 13, 185),
			new Road(31, 14, 275), new Road(31, 15, 305), new Road(31, 16, 115), new Road(31, 17, 140),
			new Road(31, 18, 175), new Road(31, 19, 215), new Road(31, 20, 300), new Road(31, 21, 360),
			new Road(31, 22, 120), new Road(31, 23, 230), new Road(31, 24, 350), new Road(31, 25, 260),
			new Road(31, 26, 160), new Road(31, 27, 355), new Road(31, 28, 190), new Road(31, 29, 350),
			new Road(31, 30, 245), new Road(31, 31, 0), new Road(31, 32, 445), new Road(31, 33, 300),
			new Road(31, 34, 295), new Road(31, 35, 335), new Road(31, 36, 310), new Road(31, 37, 225),

			new Road(32, 0, 330), new Road(32, 1, 80), new Road(32, 2, 45), new Road(32, 3, 490), new Road(32, 4, 215),
			new Road(32, 5, 285), new Road(32, 6, 450), new Road(32, 7, 400), new Road(32, 8, 270), new Road(32, 9, 65),
			new Road(32, 10, 725), new Road(32, 11, 410), new Road(32, 12, 235), new Road(32, 13, 500),
			new Road(32, 14, 330), new Road(32, 15, 355), new Road(32, 16, 375), new Road(32, 17, 550),
			new Road(32, 18, 305), new Road(32, 19, 435), new Road(32, 20, 605), new Road(32, 21, 90),
			new Road(32, 22, 360), new Road(32, 23, 480), new Road(32, 24, 635), new Road(32, 25, 400),
			new Road(32, 26, 535), new Road(32, 27, 630), new Road(32, 28, 355), new Road(32, 29, 645),
			new Road(32, 30, 210), new Road(32, 31, 445), new Road(32, 32, 0), new Road(32, 33, 175),
			new Road(32, 34, 350), new Road(32, 35, 110), new Road(32, 36, 185), new Road(32, 37, 545),

			new Road(33, 0, 175), new Road(33, 1, 145), new Road(33, 2, 195), new Road(33, 3, 335),
			new Road(33, 4, 115), new Road(33, 5, 145), new Road(33, 6, 295), new Road(33, 7, 245),
			new Road(33, 8, 100), new Road(33, 9, 115), new Road(33, 10, 585), new Road(33, 11, 260),
			new Road(33, 12, 125), new Road(33, 13, 335), new Road(33, 14, 225), new Road(33, 15, 250),
			new Road(33, 16, 210), new Road(33, 17, 420), new Road(33, 18, 165), new Road(33, 19, 285),
			new Road(33, 20, 415), new Road(33, 21, 110), new Road(33, 22, 155), new Road(33, 23, 340),
			new Road(33, 24, 505), new Road(33, 25, 235), new Road(33, 26, 385), new Road(33, 27, 495),
			new Road(33, 28, 215), new Road(33, 29, 510), new Road(33, 30, 95), new Road(33, 31, 300),
			new Road(33, 32, 175), new Road(33, 33, 0), new Road(33, 34, 255), new Road(33, 35, 85),
			new Road(33, 36, 65), new Road(33, 37, 400),

			new Road(34, 0, 110), new Road(34, 1, 260), new Road(34, 2, 310), new Road(34, 3, 315),
			new Road(34, 4, 135), new Road(34, 5, 310), new Road(34, 6, 225), new Road(34, 7, 260),
			new Road(34, 8, 210), new Road(34, 9, 280), new Road(34, 10, 495), new Road(34, 11, 190),
			new Road(34, 12, 350), new Road(34, 13, 220), new Road(34, 14, 40), new Road(34, 15, 25),
			new Road(34, 16, 200), new Road(34, 17, 400), new Road(34, 18, 135), new Road(34, 19, 125),
			new Road(34, 20, 205), new Road(34, 21, 305), new Road(34, 22, 235), new Road(34, 23, 180),
			new Road(34, 24, 405), new Road(34, 25, 40), new Road(34, 26, 305), new Road(34, 27, 345),
			new Road(34, 28, 110), new Road(34, 29, 405), new Road(34, 30, 335), new Road(34, 31, 295),
			new Road(34, 32, 350), new Road(34, 33, 255), new Road(34, 34, 0), new Road(34, 35, 320),
			new Road(34, 36, 175), new Road(34, 37, 290),

			new Road(35, 0, 240), new Road(35, 1, 110), new Road(35, 2, 140), new Road(35, 3, 380),
			new Road(35, 4, 175), new Road(35, 5, 185), new Road(35, 6, 330), new Road(35, 7, 290),
			new Road(35, 8, 175), new Road(35, 9, 75), new Road(35, 10, 655), new Road(35, 11, 310),
			new Road(35, 12, 125), new Road(35, 13, 410), new Road(35, 14, 300), new Road(35, 15, 325),
			new Road(35, 16, 280), new Road(35, 17, 465), new Road(35, 18, 220), new Road(35, 19, 365),
			new Road(35, 20, 520), new Road(35, 21, 25), new Road(35, 22, 220), new Road(35, 23, 420),
			new Road(35, 24, 570), new Road(35, 25, 315), new Road(35, 26, 450), new Road(35, 27, 580),
			new Road(35, 28, 270), new Road(35, 29, 595), new Road(35, 30, 90), new Road(35, 31, 335),
			new Road(35, 32, 110), new Road(35, 33, 85), new Road(35, 34, 320), new Road(35, 35, 0),
			new Road(35, 36, 145), new Road(35, 37, 470),

			new Road(36, 0, 145), new Road(36, 1, 110), new Road(36, 2, 165), new Road(36, 3, 350), new Road(36, 4, 45),
			new Road(36, 5, 190), new Road(36, 6, 280), new Road(36, 7, 245), new Road(36, 8, 75), new Road(36, 9, 115),
			new Road(36, 10, 545), new Road(36, 11, 240), new Road(36, 12, 200), new Road(36, 13, 315),
			new Road(36, 14, 160), new Road(36, 15, 185), new Road(36, 16, 205), new Road(36, 17, 435),
			new Road(36, 18, 120), new Road(36, 19, 255), new Road(36, 20, 400), new Road(36, 21, 140),
			new Road(36, 22, 175), new Road(36, 23, 310), new Road(36, 24, 465), new Road(36, 25, 200),
			new Road(36, 26, 360), new Road(36, 27, 455), new Road(36, 28, 160), new Road(36, 29, 465),
			new Road(36, 30, 175), new Road(36, 31, 310), new Road(36, 32, 185), new Road(36, 33, 65),
			new Road(36, 34, 175), new Road(36, 35, 145), new Road(36, 36, 0), new Road(36, 37, 365),

			new Road(37, 0, 265), new Road(37, 1, 460), new Road(37, 2, 510), new Road(37, 3, 260),
			new Road(37, 4, 375), new Road(37, 5, 335), new Road(37, 6, 115), new Road(37, 7, 245),
			new Road(37, 8, 325), new Road(37, 9, 470), new Road(37, 10, 195), new Road(37, 11, 190),
			new Road(37, 12, 390), new Road(37, 13, 55), new Road(37, 14, 340), new Road(37, 15, 315),
			new Road(37, 16, 220), new Road(37, 17, 345), new Road(37, 18, 250), new Road(37, 19, 165),
			new Road(37, 20, 120), new Road(37, 21, 480), new Road(37, 22, 280), new Road(37, 23, 85),
			new Road(37, 24, 105), new Road(37, 25, 250), new Road(37, 26, 105), new Road(37, 27, 100),
			new Road(37, 28, 190), new Road(37, 29, 105), new Road(37, 30, 415), new Road(37, 31, 225),
			new Road(37, 32, 545), new Road(37, 33, 400), new Road(37, 34, 290), new Road(37, 35, 470),
			new Road(37, 36, 365), new Road(37, 37, 0));

	/* ===================== SHORTEST PATH METHOD ===================== */
	public static Result findRoute(int src, int dest) {

		MinCostFlow flow = new MinCostFlow();

		// Add bi-directional roads
		for (Road r : roads) {
			flow.addArcWithCapacityAndUnitCost(r.from, r.to, 1, r.distance);
			flow.addArcWithCapacityAndUnitCost(r.to, r.from, 1, r.distance);
		}

		flow.setNodeSupply(src, 1);
		flow.setNodeSupply(dest, -1);

		MinCostFlowBase.Status status = flow.solve();

		if (status != MinCostFlowBase.Status.OPTIMAL) {
			return null;
		}

		long totalDistance = flow.getOptimalCost();

		// Extract route
		Map<Integer, Integer> parent = new HashMap<>();
		for (int i = 0; i < flow.getNumArcs(); i++) {
			if (flow.getFlow(i) > 0) {
				parent.put(flow.getTail(i), flow.getHead(i));
			}
		}

		List<String> path = new ArrayList<>();
		int current = src;
		path.add(DistrictMap.getName(current));

		while (current != dest) {
			current = parent.get(current);
			path.add(DistrictMap.getName(current));
		}

		return new Result(path, totalDistance);
	}

	/* ===================== MAIN METHOD ===================== */
	public static int calculateCost(String userSource, String userDestination) {

		// Load OR-Tools native libraries ONCE
		Loader.loadNativeLibraries();

		int source = DistrictMap.getId(userSource);
		int destination = DistrictMap.getId(userDestination);

		Result result = findRoute(source, destination);

		if (result != null) {
			System.out.println("Route:");
			System.out.println(String.join(" → ", result.path));
			System.out.println("THEORITICAL Distance (APPROXIMATION): " + result.distance + " km");
		} else {
			System.out.println("No route found");
			return -1;
		}
		return ((int) result.distance) * 2;
	}
}