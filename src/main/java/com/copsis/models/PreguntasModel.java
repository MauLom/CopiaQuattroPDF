 package com.copsis.models ;

    import java.util.ArrayList ;
    import java.util.List ;

    import com.copsis.clients.projections.SaludProjection ;

    public class PreguntasModel {

        public ArrayList<Preguntas> setPreguntas(List<SaludProjection> salud,boolean mujer) {
            ArrayList<Preguntas> lisPreguntas = new ArrayList<Preguntas>();

            if (salud.get(0).getPregunta13().contains("si") && mujer) {

                if (salud.get(0).getPregunta13Rd().contains("ambos")) {
                    Preguntas prg130 = new Preguntas();
                    prg130.setNumeroP("13");
                    prg130.setPregunta0Rd(salud.get(0).getPregunta13R1());
                    prg130.setPregunta0Rd1(salud.get(0).getPregunta13R2());
                    prg130.setPregunta0Rd2(salud.get(0).getPregunta13R3());
                    prg130.setPregunta0Rd3(salud.get(0).getPregunta13R4());
                    prg130.setPregunta0Rd4(salud.get(0).getPregunta13R5());

                    lisPreguntas.add(prg130);
                    Preguntas prg131 = new Preguntas();
                    prg131.setNumeroP("13");
                    prg131.setPregunta0Rd(salud.get(0).getPregunta13R6());
                    prg131.setPregunta0Rd1(salud.get(0).getPregunta13R7());
                    prg131.setPregunta0Rd2(salud.get(0).getPregunta13R8());
                    prg131.setPregunta0Rd3(salud.get(0).getPregunta13R9());
                    prg131.setPregunta0Rd4(salud.get(0).getPregunta13R10());
                    lisPreguntas.add(prg131);
                } else if (salud.get(0).getPregunta13Rd().contains("Titular")) {
                    Preguntas prg130 = new Preguntas();
                    prg130.setNumeroP("13");
                    prg130.setPregunta0Rd(salud.get(0).getPregunta13R1());
                    prg130.setPregunta0Rd1(salud.get(0).getPregunta13R2());
                    prg130.setPregunta0Rd2(salud.get(0).getPregunta13R3());
                    prg130.setPregunta0Rd3(salud.get(0).getPregunta13R4());
                    prg130.setPregunta0Rd4(salud.get(0).getPregunta13R5());
                    lisPreguntas.add(prg130);
                } else {
                    Preguntas prg131 = new Preguntas();
                    prg131.setNumeroP("13");
                    prg131.setPregunta0Rd(salud.get(0).getPregunta13R6());
                    prg131.setPregunta0Rd1(salud.get(0).getPregunta13R7());
                    prg131.setPregunta0Rd2(salud.get(0).getPregunta13R8());
                    prg131.setPregunta0Rd3(salud.get(0).getPregunta13R9());
                    prg131.setPregunta0Rd4(salud.get(0).getPregunta13R10());
                    lisPreguntas.add(prg131);
                }

            }

            if (salud.get(0).getPregunta14().contains("si")&& mujer) {
                if (salud.get(0).getPregunta14Rd().contains("ambos")) {
                    Preguntas prg140 = new Preguntas();
                    prg140.setNumeroP("14");
                    System.out.println(salud.get(0).getPregunta14R1());
                    prg140.setPregunta0Rd(salud.get(0).getPregunta14R1());
                    prg140.setPregunta0Rd1(salud.get(0).getPregunta14R2());
                    prg140.setPregunta0Rd2(salud.get(0).getPregunta14R3());
                    prg140.setPregunta0Rd3(salud.get(0).getPregunta14R4());
                    prg140.setPregunta0Rd4(salud.get(0).getPregunta14R5());
                    lisPreguntas.add(prg140);
                    Preguntas prg141 = new Preguntas();
                    prg141.setNumeroP("14");
                    prg141.setPregunta0Rd(salud.get(0).getPregunta14R6());
                    prg141.setPregunta0Rd1(salud.get(0).getPregunta14R7());
                    prg141.setPregunta0Rd2(salud.get(0).getPregunta14R8());
                    prg141.setPregunta0Rd3(salud.get(0).getPregunta14R9());
                    prg141.setPregunta0Rd4(salud.get(0).getPregunta14R10());
                    lisPreguntas.add(prg141);
                } else if (salud.get(0).getPregunta14Rd().contains("Titular")) {
                    Preguntas prg140 = new Preguntas();
                    prg140.setNumeroP("14");
                    prg140.setPregunta0Rd(salud.get(0).getPregunta14R1());
                    prg140.setPregunta0Rd1(salud.get(0).getPregunta14R2());
                    prg140.setPregunta0Rd2(salud.get(0).getPregunta14R3());
                    prg140.setPregunta0Rd3(salud.get(0).getPregunta14R4());
                    prg140.setPregunta0Rd4(salud.get(0).getPregunta14R5());
                    lisPreguntas.add(prg140);
                } else {
                    Preguntas prg141 = new Preguntas();
                    prg141.setNumeroP("14");
                    prg141.setPregunta0Rd(salud.get(0).getPregunta14R6());
                    prg141.setPregunta0Rd1(salud.get(0).getPregunta14R7());
                    prg141.setPregunta0Rd2(salud.get(0).getPregunta14R8());
                    prg141.setPregunta0Rd3(salud.get(0).getPregunta14R9());
                    prg141.setPregunta0Rd4(salud.get(0).getPregunta14R10());
                    lisPreguntas.add(prg141);
                }

            }


            //orden de preguntas de 6 a 12
            if (salud.get(0).getPregunta6().contains("si")) {
                if (salud.get(0).getPregunta6Rd().contains("ambos")) {
                    Preguntas prg6 = new Preguntas();
                    prg6.setNumeroP("6");
                    prg6.setPregunta0Rd(salud.get(0).getPregunta6R1());
                    prg6.setPregunta0Rd1(salud.get(0).getPregunta6R2());
                    prg6.setPregunta0Rd2(salud.get(0).getPregunta6R3());
                    prg6.setPregunta0Rd3(salud.get(0).getPregunta6R4());
                    prg6.setPregunta0Rd4(salud.get(0).getPregunta6R5());
                    lisPreguntas.add(prg6);
                    Preguntas prg61 = new Preguntas();
                    prg61.setNumeroP("6");
                    prg61.setPregunta0Rd(salud.get(0).getPregunta6R6());
                    prg61.setPregunta0Rd1(salud.get(0).getPregunta6R7());
                    prg61.setPregunta0Rd2(salud.get(0).getPregunta6R8());
                    prg61.setPregunta0Rd3(salud.get(0).getPregunta6R9());
                    prg61.setPregunta0Rd4(salud.get(0).getPregunta6R10());
                    lisPreguntas.add(prg61);
                } else if (salud.get(0).getPregunta6Rd().contains("Titular")) {
                    Preguntas prg6 = new Preguntas();
                    prg6.setNumeroP("6");
                    prg6.setPregunta0Rd(salud.get(0).getPregunta6R1());
                    prg6.setPregunta0Rd1(salud.get(0).getPregunta6R2());
                    prg6.setPregunta0Rd2(salud.get(0).getPregunta6R3());
                    prg6.setPregunta0Rd3(salud.get(0).getPregunta6R4());
                    prg6.setPregunta0Rd4(salud.get(0).getPregunta6R5());
                    lisPreguntas.add(prg6);
                } else {
                    Preguntas prg61 = new Preguntas();
                    prg61.setNumeroP("6");
                    prg61.setPregunta0Rd(salud.get(0).getPregunta6R6());
                    prg61.setPregunta0Rd1(salud.get(0).getPregunta6R7());
                    prg61.setPregunta0Rd2(salud.get(0).getPregunta6R8());
                    prg61.setPregunta0Rd3(salud.get(0).getPregunta6R9());
                    prg61.setPregunta0Rd4(salud.get(0).getPregunta6R10());
                    lisPreguntas.add(prg61);
                }

            }

            if (salud.get(0).getPregunta7().contains("si")) {
                if (salud.get(0).getPregunta7Rd().contains("ambos")) {
                    Preguntas prg7 = new Preguntas();
                    prg7.setNumeroP("7");
                    prg7.setPregunta0Rd(salud.get(0).getPregunta7R1());
                    prg7.setPregunta0Rd1(salud.get(0).getPregunta7R2());
                    prg7.setPregunta0Rd2(salud.get(0).getPregunta7R3());
                    prg7.setPregunta0Rd3(salud.get(0).getPregunta7R4());
                    prg7.setPregunta0Rd4(salud.get(0).getPregunta7R5());
                    lisPreguntas.add(prg7);
                    Preguntas prg71 = new Preguntas();
                    prg71.setNumeroP("7");
                    prg71.setPregunta0Rd(salud.get(0).getPregunta7R6());
                    prg71.setPregunta0Rd1(salud.get(0).getPregunta7R7());
                    prg71.setPregunta0Rd2(salud.get(0).getPregunta7R8());
                    prg71.setPregunta0Rd3(salud.get(0).getPregunta7R9());
                    prg71.setPregunta0Rd4(salud.get(0).getPregunta7R10());
                    lisPreguntas.add(prg71);
                } else if (salud.get(0).getPregunta7Rd().contains("Titular")) {
                    Preguntas prg7 = new Preguntas();
                    prg7.setNumeroP("7");
                    prg7.setPregunta0Rd(salud.get(0).getPregunta7R1());
                    prg7.setPregunta0Rd1(salud.get(0).getPregunta7R2());
                    prg7.setPregunta0Rd2(salud.get(0).getPregunta7R3());
                    prg7.setPregunta0Rd3(salud.get(0).getPregunta7R4());
                    prg7.setPregunta0Rd4(salud.get(0).getPregunta7R5());
                    lisPreguntas.add(prg7);
                } else {
                    Preguntas prg71 = new Preguntas();
                    prg71.setNumeroP("7");
                    prg71.setPregunta0Rd(salud.get(0).getPregunta7R6());
                    prg71.setPregunta0Rd1(salud.get(0).getPregunta7R7());
                    prg71.setPregunta0Rd2(salud.get(0).getPregunta7R8());
                    prg71.setPregunta0Rd3(salud.get(0).getPregunta7R9());
                    prg71.setPregunta0Rd4(salud.get(0).getPregunta7R10());
                    lisPreguntas.add(prg71);
                }

            }

            if (salud.get(0).getPregunta8().contains("si")) {
                if (salud.get(0).getPregunta8Rd().contains("ambos")) {
                    Preguntas prg8 = new Preguntas();
                    prg8.setNumeroP("8");
                    prg8.setPregunta0Rd(salud.get(0).getPregunta8R1());
                    prg8.setPregunta0Rd1(salud.get(0).getPregunta8R2());
                    prg8.setPregunta0Rd2(salud.get(0).getPregunta8R3());
                    prg8.setPregunta0Rd3(salud.get(0).getPregunta8R4());
                    prg8.setPregunta0Rd4(salud.get(0).getPregunta8R5());
                    lisPreguntas.add(prg8);
                    Preguntas prg81 = new Preguntas();
                    prg81.setNumeroP("8");
                    prg81.setPregunta0Rd(salud.get(0).getPregunta8R6());
                    prg81.setPregunta0Rd1(salud.get(0).getPregunta8R7());
                    prg81.setPregunta0Rd2(salud.get(0).getPregunta8R8());
                    prg81.setPregunta0Rd3(salud.get(0).getPregunta8R9());
                    prg81.setPregunta0Rd4(salud.get(0).getPregunta8R10());
                    lisPreguntas.add(prg81);
                } else if (salud.get(0).getPregunta8Rd().contains("Titular")) {
                    Preguntas prg8 = new Preguntas();
                    prg8.setNumeroP("8");
                    prg8.setPregunta0Rd(salud.get(0).getPregunta8R1());
                    prg8.setPregunta0Rd1(salud.get(0).getPregunta8R2());
                    prg8.setPregunta0Rd2(salud.get(0).getPregunta8R3());
                    prg8.setPregunta0Rd3(salud.get(0).getPregunta8R4());
                    prg8.setPregunta0Rd4(salud.get(0).getPregunta8R5());
                    lisPreguntas.add(prg8);
                } else {
                    Preguntas prg81 = new Preguntas();
                    prg81.setNumeroP("8");
                    prg81.setPregunta0Rd(salud.get(0).getPregunta8R6());
                    prg81.setPregunta0Rd1(salud.get(0).getPregunta8R7());
                    prg81.setPregunta0Rd2(salud.get(0).getPregunta8R8());
                    prg81.setPregunta0Rd3(salud.get(0).getPregunta8R9());
                    prg81.setPregunta0Rd4(salud.get(0).getPregunta8R10());
                    lisPreguntas.add(prg81);
                }

            }

            if (salud.get(0).getPregunta9().contains("si")) {
                if (salud.get(0).getPregunta9Rd().contains("ambos")) {
                    Preguntas prg9 = new Preguntas();
                    prg9.setNumeroP("9");
                    prg9.setPregunta0Rd(salud.get(0).getPregunta9R1());
                    prg9.setPregunta0Rd1(salud.get(0).getPregunta9R2());
                    prg9.setPregunta0Rd2(salud.get(0).getPregunta9R3());
                    prg9.setPregunta0Rd3(salud.get(0).getPregunta9R4());
                    prg9.setPregunta0Rd4(salud.get(0).getPregunta9R5());
                    lisPreguntas.add(prg9);
                    Preguntas prg91 = new Preguntas();
                    prg91.setNumeroP("9");
                    prg91.setPregunta0Rd(salud.get(0).getPregunta9R6());
                    prg91.setPregunta0Rd1(salud.get(0).getPregunta9R7());
                    prg91.setPregunta0Rd2(salud.get(0).getPregunta9R8());
                    prg91.setPregunta0Rd3(salud.get(0).getPregunta9R9());
                    prg91.setPregunta0Rd4(salud.get(0).getPregunta9R10());
                    lisPreguntas.add(prg91);
                } else if (salud.get(0).getPregunta9Rd().contains("Titular")) {
                    Preguntas prg9 = new Preguntas();
                    prg9.setNumeroP("9");
                    prg9.setPregunta0Rd(salud.get(0).getPregunta9R1());
                    prg9.setPregunta0Rd1(salud.get(0).getPregunta9R2());
                    prg9.setPregunta0Rd2(salud.get(0).getPregunta9R3());
                    prg9.setPregunta0Rd3(salud.get(0).getPregunta9R4());
                    prg9.setPregunta0Rd4(salud.get(0).getPregunta9R5());
                    lisPreguntas.add(prg9);
                } else {
                    Preguntas prg91 = new Preguntas();
                    prg91.setNumeroP("9");
                    prg91.setPregunta0Rd(salud.get(0).getPregunta9R6());
                    prg91.setPregunta0Rd1(salud.get(0).getPregunta9R7());
                    prg91.setPregunta0Rd2(salud.get(0).getPregunta9R8());
                    prg91.setPregunta0Rd3(salud.get(0).getPregunta9R9());
                    prg91.setPregunta0Rd4(salud.get(0).getPregunta9R10());
                    lisPreguntas.add(prg91);
                }

            }

            if (salud.get(0).getPregunta10().contains("si")) {
                if (salud.get(0).getPregunta10Rd().contains("ambos")) {
                    Preguntas prg10 = new Preguntas();
                    prg10.setNumeroP("10");
                    prg10.setPregunta0Rd(salud.get(0).getPregunta10R1());
                    prg10.setPregunta0Rd1(salud.get(0).getPregunta10R2());
                    prg10.setPregunta0Rd2(salud.get(0).getPregunta10R3());
                    prg10.setPregunta0Rd3(salud.get(0).getPregunta10R4());
                    prg10.setPregunta0Rd4(salud.get(0).getPregunta10R5());
                    lisPreguntas.add(prg10);
                    Preguntas prg101 = new Preguntas();
                    prg101.setNumeroP("10");
                    prg101.setPregunta0Rd(salud.get(0).getPregunta10R6());
                    prg101.setPregunta0Rd1(salud.get(0).getPregunta10R7());
                    prg101.setPregunta0Rd2(salud.get(0).getPregunta10R8());
                    prg101.setPregunta0Rd3(salud.get(0).getPregunta10R9());
                    prg101.setPregunta0Rd4(salud.get(0).getPregunta10R10());
                    lisPreguntas.add(prg101);
                } else if (salud.get(0).getPregunta10Rd().contains("Titular")) {
                    Preguntas prg10 = new Preguntas();
                    prg10.setNumeroP("10");
                    prg10.setPregunta0Rd(salud.get(0).getPregunta10R1());
                    prg10.setPregunta0Rd1(salud.get(0).getPregunta10R2());
                    prg10.setPregunta0Rd2(salud.get(0).getPregunta10R3());
                    prg10.setPregunta0Rd3(salud.get(0).getPregunta10R4());
                    prg10.setPregunta0Rd4(salud.get(0).getPregunta10R5());
                    lisPreguntas.add(prg10);
                } else {
                    Preguntas prg101 = new Preguntas();
                    prg101.setNumeroP("10");
                    prg101.setPregunta0Rd(salud.get(0).getPregunta10R6());
                    prg101.setPregunta0Rd1(salud.get(0).getPregunta10R7());
                    prg101.setPregunta0Rd2(salud.get(0).getPregunta10R8());
                    prg101.setPregunta0Rd3(salud.get(0).getPregunta10R9());
                    prg101.setPregunta0Rd4(salud.get(0).getPregunta10R10());
                    lisPreguntas.add(prg101);
                }

            }

            if (salud.get(0).getPregunta11().contains("si")) {
                if (salud.get(0).getPregunta11Rd().contains("ambos")) {
                    Preguntas prg11 = new Preguntas();
                    prg11.setNumeroP("11");
                    prg11.setPregunta0Rd(salud.get(0).getPregunta11R1());
                    prg11.setPregunta0Rd1(salud.get(0).getPregunta11R2());
                    prg11.setPregunta0Rd2(salud.get(0).getPregunta11R3());
                    prg11.setPregunta0Rd3(salud.get(0).getPregunta11R4());
                    prg11.setPregunta0Rd4(salud.get(0).getPregunta11R5());
                    lisPreguntas.add(prg11);
                    Preguntas prg111 = new Preguntas();
                    prg111.setNumeroP("11");
                    prg111.setPregunta0Rd(salud.get(0).getPregunta11R6());
                    prg111.setPregunta0Rd1(salud.get(0).getPregunta11R7());
                    prg111.setPregunta0Rd2(salud.get(0).getPregunta11R8());
                    prg111.setPregunta0Rd3(salud.get(0).getPregunta11R9());
                    prg111.setPregunta0Rd4(salud.get(0).getPregunta11R10());
                    lisPreguntas.add(prg111);
                } else if (salud.get(0).getPregunta11Rd().contains("Titular")) {
                    Preguntas prg11 = new Preguntas();
                    prg11.setNumeroP("11");
                    prg11.setPregunta0Rd(salud.get(0).getPregunta11R1());
                    prg11.setPregunta0Rd1(salud.get(0).getPregunta11R2());
                    prg11.setPregunta0Rd2(salud.get(0).getPregunta11R3());
                    prg11.setPregunta0Rd3(salud.get(0).getPregunta11R4());
                    prg11.setPregunta0Rd4(salud.get(0).getPregunta11R5());
                    lisPreguntas.add(prg11);
                } else {
                    Preguntas prg111 = new Preguntas();
                    prg111.setNumeroP("11");
                    prg111.setPregunta0Rd(salud.get(0).getPregunta11R6());
                    prg111.setPregunta0Rd1(salud.get(0).getPregunta11R7());
                    prg111.setPregunta0Rd2(salud.get(0).getPregunta11R8());
                    prg111.setPregunta0Rd3(salud.get(0).getPregunta11R9());
                    prg111.setPregunta0Rd4(salud.get(0).getPregunta11R10());
                    lisPreguntas.add(prg111);
                }

                if (salud.get(0).getPregunta12().contains("si")) {
                    if (salud.get(0).getPregunta12Rd().contains("ambos")) {
                        Preguntas prg12 = new Preguntas();
                        prg12.setNumeroP("12");
                        prg12.setPregunta0Rd(salud.get(0).getPregunta12R1());
                        prg12.setPregunta0Rd1(salud.get(0).getPregunta12R2());
                        prg12.setPregunta0Rd2(salud.get(0).getPregunta12R3());
                        prg12.setPregunta0Rd3(salud.get(0).getPregunta12R4());
                        prg12.setPregunta0Rd4(salud.get(0).getPregunta12R5());
                        lisPreguntas.add(prg12);
                        Preguntas prg121 = new Preguntas();
                        prg121.setNumeroP("12");
                        prg121.setPregunta0Rd(salud.get(0).getPregunta12R6());
                        prg121.setPregunta0Rd1(salud.get(0).getPregunta12R7());
                        prg121.setPregunta0Rd2(salud.get(0).getPregunta12R8());
                        prg121.setPregunta0Rd3(salud.get(0).getPregunta12R9());
                        prg121.setPregunta0Rd4(salud.get(0).getPregunta12R10());
                        lisPreguntas.add(prg121);
                    } else if (salud.get(0).getPregunta12Rd().contains("Titular")) {
                        Preguntas prg12 = new Preguntas();
                        prg12.setNumeroP("12");
                        prg12.setPregunta0Rd(salud.get(0).getPregunta12R1());
                        prg12.setPregunta0Rd1(salud.get(0).getPregunta12R2());
                        prg12.setPregunta0Rd2(salud.get(0).getPregunta12R3());
                        prg12.setPregunta0Rd3(salud.get(0).getPregunta12R4());
                        prg12.setPregunta0Rd4(salud.get(0).getPregunta12R5());
                        lisPreguntas.add(prg12);
                    } else {
                        Preguntas prg121 = new Preguntas();
                        prg121.setNumeroP("12");
                        prg121.setPregunta0Rd(salud.get(0).getPregunta12R6());
                        prg121.setPregunta0Rd1(salud.get(0).getPregunta12R7());
                        prg121.setPregunta0Rd2(salud.get(0).getPregunta12R8());
                        prg121.setPregunta0Rd3(salud.get(0).getPregunta12R9());
                        prg121.setPregunta0Rd4(salud.get(0).getPregunta12R10());
                        lisPreguntas.add(prg121);
                    }

                }
            }

            return lisPreguntas;
        }

    }