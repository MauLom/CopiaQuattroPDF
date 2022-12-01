 package com.copsis.models ;

    import java.util.ArrayList ;
    import java.util.List ;

    import com.copsis.clients.projections.SaludProjection ;

    public class PreguntasModel {

        public ArrayList<Preguntas> setPreguntas(List<SaludProjection> salud,boolean mujer) {
            ArrayList<Preguntas> lisPreguntas = new ArrayList<Preguntas>();
            
            if (salud.get(0).getPregunta18().contains("si") && mujer) {
              
                   Preguntas prg180 = new Preguntas();
                   prg180.setNumeroP("13");
                   prg180.setPregunta0Rd(salud.get(0).getPregunta18R1());
                   prg180.setPregunta0Rd1(salud.get(0).getPregunta18R2());
                   prg180.setPregunta0Rd2(salud.get(0).getPregunta18R3());
                   prg180.setPregunta0Rd3(salud.get(0).getPregunta18R4());
                   prg180.setPregunta0Rd4(salud.get(0).getPregunta18R5());
                   lisPreguntas.add(prg180);
               
            }
            
            if (salud.get(0).getPregunta19().contains("si") && mujer) {
                
                Preguntas prg190 = new Preguntas();
                prg190.setNumeroP("14");
                prg190.setPregunta0Rd(salud.get(0).getPregunta19R1());
                prg190.setPregunta0Rd1(salud.get(0).getPregunta19R2());
                prg190.setPregunta0Rd2(salud.get(0).getPregunta19R3());
                prg190.setPregunta0Rd3(salud.get(0).getPregunta19R4());
                prg190.setPregunta0Rd4(salud.get(0).getPregunta19R5());
                lisPreguntas.add(prg190);
            
            }


        

            //orden de preguntas de 6 a 12
            if (salud.get(0).getPregunta6().contains("si")) {
                if (salud.get(0).getPregunta6Rd().contains("ambos")) {
                    Preguntas prg6 = new Preguntas();
                    prg6.setNumeroP("1");
                    prg6.setPregunta0Rd(salud.get(0).getPregunta6R1());
                    prg6.setPregunta0Rd1(salud.get(0).getPregunta6R2());
                    prg6.setPregunta0Rd2(salud.get(0).getPregunta6R3());
                    prg6.setPregunta0Rd3(salud.get(0).getPregunta6R4());
                    prg6.setPregunta0Rd4(salud.get(0).getPregunta6R5());
                    lisPreguntas.add(prg6);
                    Preguntas prg61 = new Preguntas();
                    prg61.setNumeroP("1");
                    prg61.setPregunta0Rd(salud.get(0).getPregunta6R6());
                    prg61.setPregunta0Rd1(salud.get(0).getPregunta6R7());
                    prg61.setPregunta0Rd2(salud.get(0).getPregunta6R8());
                    prg61.setPregunta0Rd3(salud.get(0).getPregunta6R9());
                    prg61.setPregunta0Rd4(salud.get(0).getPregunta6R10());
                    lisPreguntas.add(prg61);
                } else if (salud.get(0).getPregunta6Rd().contains("Titular")) {
                    Preguntas prg6 = new Preguntas();
                    prg6.setNumeroP("1");
                    prg6.setPregunta0Rd(salud.get(0).getPregunta6R1());
                    prg6.setPregunta0Rd1(salud.get(0).getPregunta6R2());
                    prg6.setPregunta0Rd2(salud.get(0).getPregunta6R3());
                    prg6.setPregunta0Rd3(salud.get(0).getPregunta6R4());
                    prg6.setPregunta0Rd4(salud.get(0).getPregunta6R5());
                    lisPreguntas.add(prg6);
                } else {
                    Preguntas prg61 = new Preguntas();
                    prg61.setNumeroP("1");
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
                    prg7.setNumeroP("2");
                    prg7.setPregunta0Rd(salud.get(0).getPregunta7R1());
                    prg7.setPregunta0Rd1(salud.get(0).getPregunta7R2());
                    prg7.setPregunta0Rd2(salud.get(0).getPregunta7R3());
                    prg7.setPregunta0Rd3(salud.get(0).getPregunta7R4());
                    prg7.setPregunta0Rd4(salud.get(0).getPregunta7R5());
                    lisPreguntas.add(prg7);
                    Preguntas prg71 = new Preguntas();
                    prg71.setNumeroP("2");
                    prg71.setPregunta0Rd(salud.get(0).getPregunta7R6());
                    prg71.setPregunta0Rd1(salud.get(0).getPregunta7R7());
                    prg71.setPregunta0Rd2(salud.get(0).getPregunta7R8());
                    prg71.setPregunta0Rd3(salud.get(0).getPregunta7R9());
                    prg71.setPregunta0Rd4(salud.get(0).getPregunta7R10());
                    lisPreguntas.add(prg71);
                } else if (salud.get(0).getPregunta7Rd().contains("Titular")) {
                    Preguntas prg7 = new Preguntas();
                    prg7.setNumeroP("2");
                    prg7.setPregunta0Rd(salud.get(0).getPregunta7R1());
                    prg7.setPregunta0Rd1(salud.get(0).getPregunta7R2());
                    prg7.setPregunta0Rd2(salud.get(0).getPregunta7R3());
                    prg7.setPregunta0Rd3(salud.get(0).getPregunta7R4());
                    prg7.setPregunta0Rd4(salud.get(0).getPregunta7R5());
                    lisPreguntas.add(prg7);
                } else {
                    Preguntas prg71 = new Preguntas();
                    prg71.setNumeroP("2");
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
                    prg8.setNumeroP("3");
                    prg8.setPregunta0Rd(salud.get(0).getPregunta8R1());
                    prg8.setPregunta0Rd1(salud.get(0).getPregunta8R2());
                    prg8.setPregunta0Rd2(salud.get(0).getPregunta8R3());
                    prg8.setPregunta0Rd3(salud.get(0).getPregunta8R4());
                    prg8.setPregunta0Rd4(salud.get(0).getPregunta8R5());
                    lisPreguntas.add(prg8);
                    Preguntas prg81 = new Preguntas();
                    prg81.setNumeroP("3");
                    prg81.setPregunta0Rd(salud.get(0).getPregunta8R6());
                    prg81.setPregunta0Rd1(salud.get(0).getPregunta8R7());
                    prg81.setPregunta0Rd2(salud.get(0).getPregunta8R8());
                    prg81.setPregunta0Rd3(salud.get(0).getPregunta8R9());
                    prg81.setPregunta0Rd4(salud.get(0).getPregunta8R10());
                    lisPreguntas.add(prg81);
                } else if (salud.get(0).getPregunta8Rd().contains("Titular")) {
                    Preguntas prg8 = new Preguntas();
                    prg8.setNumeroP("3");
                    prg8.setPregunta0Rd(salud.get(0).getPregunta8R1());
                    prg8.setPregunta0Rd1(salud.get(0).getPregunta8R2());
                    prg8.setPregunta0Rd2(salud.get(0).getPregunta8R3());
                    prg8.setPregunta0Rd3(salud.get(0).getPregunta8R4());
                    prg8.setPregunta0Rd4(salud.get(0).getPregunta8R5());
                    lisPreguntas.add(prg8);
                } else {
                    Preguntas prg81 = new Preguntas();
                    prg81.setNumeroP("3");
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
                    prg9.setNumeroP("4");
                    prg9.setPregunta0Rd(salud.get(0).getPregunta9R1());
                    prg9.setPregunta0Rd1(salud.get(0).getPregunta9R2());
                    prg9.setPregunta0Rd2(salud.get(0).getPregunta9R3());
                    prg9.setPregunta0Rd3(salud.get(0).getPregunta9R4());
                    prg9.setPregunta0Rd4(salud.get(0).getPregunta9R5());
                    lisPreguntas.add(prg9);
                    Preguntas prg91 = new Preguntas();
                    prg91.setNumeroP("4");
                    prg91.setPregunta0Rd(salud.get(0).getPregunta9R6());
                    prg91.setPregunta0Rd1(salud.get(0).getPregunta9R7());
                    prg91.setPregunta0Rd2(salud.get(0).getPregunta9R8());
                    prg91.setPregunta0Rd3(salud.get(0).getPregunta9R9());
                    prg91.setPregunta0Rd4(salud.get(0).getPregunta9R10());
                    lisPreguntas.add(prg91);
                } else if (salud.get(0).getPregunta9Rd().contains("Titular")) {
                    Preguntas prg9 = new Preguntas();
                    prg9.setNumeroP("4");
                    prg9.setPregunta0Rd(salud.get(0).getPregunta9R1());
                    prg9.setPregunta0Rd1(salud.get(0).getPregunta9R2());
                    prg9.setPregunta0Rd2(salud.get(0).getPregunta9R3());
                    prg9.setPregunta0Rd3(salud.get(0).getPregunta9R4());
                    prg9.setPregunta0Rd4(salud.get(0).getPregunta9R5());
                    lisPreguntas.add(prg9);
                } else {
                    Preguntas prg91 = new Preguntas();
                    prg91.setNumeroP("4");
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
                    prg10.setNumeroP("5");
                    prg10.setPregunta0Rd(salud.get(0).getPregunta10R1());
                    prg10.setPregunta0Rd1(salud.get(0).getPregunta10R2());
                    prg10.setPregunta0Rd2(salud.get(0).getPregunta10R3());
                    prg10.setPregunta0Rd3(salud.get(0).getPregunta10R4());
                    prg10.setPregunta0Rd4(salud.get(0).getPregunta10R5());
                    lisPreguntas.add(prg10);
                    Preguntas prg101 = new Preguntas();
                    prg101.setNumeroP("5");
                    prg101.setPregunta0Rd(salud.get(0).getPregunta10R6());
                    prg101.setPregunta0Rd1(salud.get(0).getPregunta10R7());
                    prg101.setPregunta0Rd2(salud.get(0).getPregunta10R8());
                    prg101.setPregunta0Rd3(salud.get(0).getPregunta10R9());
                    prg101.setPregunta0Rd4(salud.get(0).getPregunta10R10());
                    lisPreguntas.add(prg101);
                } else if (salud.get(0).getPregunta10Rd().contains("Titular")) {
                    Preguntas prg10 = new Preguntas();
                    prg10.setNumeroP("5");
                    prg10.setPregunta0Rd(salud.get(0).getPregunta10R1());
                    prg10.setPregunta0Rd1(salud.get(0).getPregunta10R2());
                    prg10.setPregunta0Rd2(salud.get(0).getPregunta10R3());
                    prg10.setPregunta0Rd3(salud.get(0).getPregunta10R4());
                    prg10.setPregunta0Rd4(salud.get(0).getPregunta10R5());
                    lisPreguntas.add(prg10);
                } else {
                    Preguntas prg101 = new Preguntas();
                    prg101.setNumeroP("5");
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
                    prg11.setNumeroP("6");
                    prg11.setPregunta0Rd(salud.get(0).getPregunta11R1());
                    prg11.setPregunta0Rd1(salud.get(0).getPregunta11R2());
                    prg11.setPregunta0Rd2(salud.get(0).getPregunta11R3());
                    prg11.setPregunta0Rd3(salud.get(0).getPregunta11R4());
                    prg11.setPregunta0Rd4(salud.get(0).getPregunta11R5());
                    lisPreguntas.add(prg11);
                    Preguntas prg111 = new Preguntas();
                    prg111.setNumeroP("6");
                    prg111.setPregunta0Rd(salud.get(0).getPregunta11R6());
                    prg111.setPregunta0Rd1(salud.get(0).getPregunta11R7());
                    prg111.setPregunta0Rd2(salud.get(0).getPregunta11R8());
                    prg111.setPregunta0Rd3(salud.get(0).getPregunta11R9());
                    prg111.setPregunta0Rd4(salud.get(0).getPregunta11R10());
                    lisPreguntas.add(prg111);
                } else if (salud.get(0).getPregunta11Rd().contains("Titular")) {
                    Preguntas prg11 = new Preguntas();
                    prg11.setNumeroP("6");
                    prg11.setPregunta0Rd(salud.get(0).getPregunta11R1());
                    prg11.setPregunta0Rd1(salud.get(0).getPregunta11R2());
                    prg11.setPregunta0Rd2(salud.get(0).getPregunta11R3());
                    prg11.setPregunta0Rd3(salud.get(0).getPregunta11R4());
                    prg11.setPregunta0Rd4(salud.get(0).getPregunta11R5());
                    lisPreguntas.add(prg11);
                } else {
                    Preguntas prg111 = new Preguntas();
                    prg111.setNumeroP("6");
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
                        prg12.setNumeroP("7");
                        prg12.setPregunta0Rd(salud.get(0).getPregunta12R1());
                        prg12.setPregunta0Rd1(salud.get(0).getPregunta12R2());
                        prg12.setPregunta0Rd2(salud.get(0).getPregunta12R3());
                        prg12.setPregunta0Rd3(salud.get(0).getPregunta12R4());
                        prg12.setPregunta0Rd4(salud.get(0).getPregunta12R5());
                        lisPreguntas.add(prg12);
                        Preguntas prg121 = new Preguntas();
                        prg121.setNumeroP("7");
                        prg121.setPregunta0Rd(salud.get(0).getPregunta12R6());
                        prg121.setPregunta0Rd1(salud.get(0).getPregunta12R7());
                        prg121.setPregunta0Rd2(salud.get(0).getPregunta12R8());
                        prg121.setPregunta0Rd3(salud.get(0).getPregunta12R9());
                        prg121.setPregunta0Rd4(salud.get(0).getPregunta12R10());
                        lisPreguntas.add(prg121);
                    } else if (salud.get(0).getPregunta12Rd().contains("Titular")) {
                        Preguntas prg12 = new Preguntas();
                        prg12.setNumeroP("7");
                        prg12.setPregunta0Rd(salud.get(0).getPregunta12R1());
                        prg12.setPregunta0Rd1(salud.get(0).getPregunta12R2());
                        prg12.setPregunta0Rd2(salud.get(0).getPregunta12R3());
                        prg12.setPregunta0Rd3(salud.get(0).getPregunta12R4());
                        prg12.setPregunta0Rd4(salud.get(0).getPregunta12R5());
                        lisPreguntas.add(prg12);
                    } else {
                        Preguntas prg121 = new Preguntas();
                        prg121.setNumeroP("7");
                        prg121.setPregunta0Rd(salud.get(0).getPregunta12R6());
                        prg121.setPregunta0Rd1(salud.get(0).getPregunta12R7());
                        prg121.setPregunta0Rd2(salud.get(0).getPregunta12R8());
                        prg121.setPregunta0Rd3(salud.get(0).getPregunta12R9());
                        prg121.setPregunta0Rd4(salud.get(0).getPregunta12R10());
                        lisPreguntas.add(prg121);
                    }

                }
            }
            
            if(salud.get(0).getPregunta13().contains("si") ) {                     
                if(salud.get(0).getPregunta13Rd().contains("ambos")){              
                    Preguntas prg13 = new Preguntas();              
                    prg13.setNumeroP("8");
                    prg13.setPregunta0Rd(salud.get(0).getPregunta13R1());
                    prg13.setPregunta0Rd1(salud.get(0).getPregunta13R2());
                    prg13.setPregunta0Rd2(salud.get(0).getPregunta13R3());
                    prg13.setPregunta0Rd3(salud.get(0).getPregunta13R4());
                    prg13.setPregunta0Rd4(salud.get(0).getPregunta13R5());
                    lisPreguntas.add(prg13);
                    Preguntas prg131 = new Preguntas();            
                    prg131.setNumeroP("8");
                    prg131.setPregunta0Rd(salud.get(0).getPregunta13R6());
                    prg131.setPregunta0Rd1(salud.get(0).getPregunta13R7());
                    prg131.setPregunta0Rd2(salud.get(0).getPregunta13R8());
                    prg131.setPregunta0Rd3(salud.get(0).getPregunta13R9());
                    prg131.setPregunta0Rd4(salud.get(0).getPregunta13R10());
                    lisPreguntas.add(prg131);
                } else   if(salud.get(0).getPregunta13Rd().contains("Titular")){              
                  Preguntas prg13 = new Preguntas();              
                  prg13.setNumeroP("8");
                  prg13.setPregunta0Rd(salud.get(0).getPregunta13R1());
                  prg13.setPregunta0Rd1(salud.get(0).getPregunta13R2());
                  prg13.setPregunta0Rd2(salud.get(0).getPregunta13R3());
                  prg13.setPregunta0Rd3(salud.get(0).getPregunta13R4());
                  prg13.setPregunta0Rd4(salud.get(0).getPregunta13R5());
                  lisPreguntas.add(prg13);
                }else {
                  Preguntas prg131 = new Preguntas();            
                  prg131.setNumeroP("8");
                  prg131.setPregunta0Rd(salud.get(0).getPregunta13R6());
                  prg131.setPregunta0Rd1(salud.get(0).getPregunta13R7());
                  prg131.setPregunta0Rd2(salud.get(0).getPregunta13R8());
                  prg131.setPregunta0Rd3(salud.get(0).getPregunta13R9());
                  prg131.setPregunta0Rd4(salud.get(0).getPregunta13R10());
                  lisPreguntas.add(prg131);
                }
      
             }
            
            
            if(salud.get(0).getPregunta14().contains("si") ) {                     
                if(salud.get(0).getPregunta14Rd().contains("ambos")){              
                    Preguntas prg14 = new Preguntas();              
                    prg14.setNumeroP("9");
                    prg14.setPregunta0Rd(salud.get(0).getPregunta14R1());
                    prg14.setPregunta0Rd1(salud.get(0).getPregunta14R2());
                    prg14.setPregunta0Rd2(salud.get(0).getPregunta14R3());
                    prg14.setPregunta0Rd3(salud.get(0).getPregunta14R4());
                    prg14.setPregunta0Rd4(salud.get(0).getPregunta14R5());
                    lisPreguntas.add(prg14);
                    Preguntas prg141 = new Preguntas();            
                    prg141.setNumeroP("9");
                    prg141.setPregunta0Rd(salud.get(0).getPregunta14R6());
                    prg141.setPregunta0Rd1(salud.get(0).getPregunta14R7());
                    prg141.setPregunta0Rd2(salud.get(0).getPregunta14R8());
                    prg141.setPregunta0Rd3(salud.get(0).getPregunta14R9());
                    prg141.setPregunta0Rd4(salud.get(0).getPregunta14R10());
                    lisPreguntas.add(prg141);
                } else   if(salud.get(0).getPregunta14Rd().contains("Titular")){              
                  Preguntas prg14 = new Preguntas();              
                  prg14.setNumeroP("9");
                  prg14.setPregunta0Rd(salud.get(0).getPregunta14R1());
                  prg14.setPregunta0Rd1(salud.get(0).getPregunta14R2());
                  prg14.setPregunta0Rd2(salud.get(0).getPregunta14R3());
                  prg14.setPregunta0Rd3(salud.get(0).getPregunta14R4());
                  prg14.setPregunta0Rd4(salud.get(0).getPregunta14R5());
                  lisPreguntas.add(prg14);
                }else {
                  Preguntas prg141 = new Preguntas();            
                  prg141.setNumeroP("9");
                  prg141.setPregunta0Rd(salud.get(0).getPregunta14R6());
                  prg141.setPregunta0Rd1(salud.get(0).getPregunta14R7());
                  prg141.setPregunta0Rd2(salud.get(0).getPregunta14R8());
                  prg141.setPregunta0Rd3(salud.get(0).getPregunta14R9());
                  prg141.setPregunta0Rd4(salud.get(0).getPregunta14R10());
                  lisPreguntas.add(prg141);
                }
      
             }
            
            
            
            if(salud.get(0).getPregunta15().contains("si") ) {                     
                if(salud.get(0).getPregunta15Rd().contains("ambos")){              
                    Preguntas prg15 = new Preguntas();              
                    prg15.setNumeroP("10");
                    prg15.setPregunta0Rd(salud.get(0).getPregunta15Rd1());
                    prg15.setPregunta0Rd1(salud.get(0).getPregunta15Rd2());
                    prg15.setPregunta0Rd2(salud.get(0).getPregunta15Rd3());
                    prg15.setPregunta0Rd3(salud.get(0).getPregunta15Rd4());
                    prg15.setPregunta0Rd4(salud.get(0).getPregunta15Rd5());
                    lisPreguntas.add(prg15);
                    Preguntas prg151 = new Preguntas();            
                    prg151.setNumeroP("10");
                    prg151.setPregunta0Rd(salud.get(0).getPregunta15Rd6());
                    prg151.setPregunta0Rd1(salud.get(0).getPregunta15Rd7());
                    prg151.setPregunta0Rd2(salud.get(0).getPregunta15Rd8());
                    prg151.setPregunta0Rd3(salud.get(0).getPregunta15Rd9());
                    prg151.setPregunta0Rd4(salud.get(0).getPregunta15Rd10());
                    lisPreguntas.add(prg151);
                } else   if(salud.get(0).getPregunta15Rd().contains("Titular")){              
                  Preguntas prg15 = new Preguntas();              
                  prg15.setNumeroP("10");
                  prg15.setPregunta0Rd(salud.get(0).getPregunta15Rd1());
                  prg15.setPregunta0Rd1(salud.get(0).getPregunta15Rd2());
                  prg15.setPregunta0Rd2(salud.get(0).getPregunta15Rd3());
                  prg15.setPregunta0Rd3(salud.get(0).getPregunta15Rd4());
                  prg15.setPregunta0Rd4(salud.get(0).getPregunta15Rd5());
                  lisPreguntas.add(prg15);
                }else {
                  Preguntas prg151 = new Preguntas();            
                  prg151.setNumeroP("10");
                  prg151.setPregunta0Rd(salud.get(0).getPregunta15Rd6());
                  prg151.setPregunta0Rd1(salud.get(0).getPregunta15Rd7());
                  prg151.setPregunta0Rd2(salud.get(0).getPregunta15Rd8());
                  prg151.setPregunta0Rd3(salud.get(0).getPregunta15Rd9());
                  prg151.setPregunta0Rd4(salud.get(0).getPregunta15Rd10());
                  lisPreguntas.add(prg151);
                }
      
             }
            
            if(salud.get(0).getPregunta16().contains("si") ) {                     
                if(salud.get(0).getPregunta16Rd().contains("ambos")){              
                    Preguntas prg16 = new Preguntas();              
                    prg16.setNumeroP("11");
                    prg16.setPregunta0Rd(salud.get(0).getPregunta16R1());
                    prg16.setPregunta0Rd1(salud.get(0).getPregunta16R2());
                    prg16.setPregunta0Rd2(salud.get(0).getPregunta16R3());
                    prg16.setPregunta0Rd3(salud.get(0).getPregunta16R4());
                    prg16.setPregunta0Rd4(salud.get(0).getPregunta16R5());
                    lisPreguntas.add(prg16);
                    Preguntas prg161 = new Preguntas();            
                    prg161.setNumeroP("11");
                    prg161.setPregunta0Rd(salud.get(0).getPregunta16R6());
                    prg161.setPregunta0Rd1(salud.get(0).getPregunta16R7());
                    prg161.setPregunta0Rd2(salud.get(0).getPregunta16R8());
                    prg161.setPregunta0Rd3(salud.get(0).getPregunta16R9());
                    prg161.setPregunta0Rd4(salud.get(0).getPregunta16R10());
                    lisPreguntas.add(prg161);
                } else   if(salud.get(0).getPregunta16Rd().contains("Titular")){              
                  Preguntas prg16 = new Preguntas();              
                  prg16.setNumeroP("11");
                  prg16.setPregunta0Rd(salud.get(0).getPregunta16R1());
                  prg16.setPregunta0Rd1(salud.get(0).getPregunta16R2());
                  prg16.setPregunta0Rd2(salud.get(0).getPregunta16R3());
                  prg16.setPregunta0Rd3(salud.get(0).getPregunta16R4());
                  prg16.setPregunta0Rd4(salud.get(0).getPregunta16R5());
                  lisPreguntas.add(prg16);
                }else {
                  Preguntas prg161 = new Preguntas();            
                  prg161.setNumeroP("11");
                  prg161.setPregunta0Rd(salud.get(0).getPregunta16R6());
                  prg161.setPregunta0Rd1(salud.get(0).getPregunta16R7());
                  prg161.setPregunta0Rd2(salud.get(0).getPregunta16R8());
                  prg161.setPregunta0Rd3(salud.get(0).getPregunta16R9());
                  prg161.setPregunta0Rd4(salud.get(0).getPregunta16R10());
                  lisPreguntas.add(prg161);
                }
      
             }
            
            if(salud.get(0).getPregunta17().contains("si") ) {                     
                if(salud.get(0).getPregunta17Rd().contains("ambos")){              
                    Preguntas prg17 = new Preguntas();              
                    prg17.setNumeroP("12");
                    prg17.setPregunta0Rd(salud.get(0).getPregunta17Rd1());
                    prg17.setPregunta0Rd1(salud.get(0).getPregunta17Rd2());
                    prg17.setPregunta0Rd2(salud.get(0).getPregunta17Rd3());
                    prg17.setPregunta0Rd3(salud.get(0).getPregunta17Rd4());
                    prg17.setPregunta0Rd4(salud.get(0).getPregunta17Rd5());
                    lisPreguntas.add(prg17);
                    Preguntas prg171 = new Preguntas();            
                    prg171.setNumeroP("12");
                    prg171.setPregunta0Rd(salud.get(0).getPregunta17Rd6());
                    prg171.setPregunta0Rd1(salud.get(0).getPregunta17Rd7());
                    prg171.setPregunta0Rd2(salud.get(0).getPregunta17Rd8());
                    prg171.setPregunta0Rd3(salud.get(0).getPregunta17Rd9());
                    prg171.setPregunta0Rd4(salud.get(0).getPregunta17Rd10());
                    lisPreguntas.add(prg171);
                } else   if(salud.get(0).getPregunta17Rd().contains("Titular")){              
                  Preguntas prg17 = new Preguntas();              
                  prg17.setNumeroP("12");
                  prg17.setPregunta0Rd(salud.get(0).getPregunta17Rd1());
                  prg17.setPregunta0Rd1(salud.get(0).getPregunta17Rd2());
                  prg17.setPregunta0Rd2(salud.get(0).getPregunta17Rd3());
                  prg17.setPregunta0Rd3(salud.get(0).getPregunta17Rd4());
                  prg17.setPregunta0Rd4(salud.get(0).getPregunta17Rd5());
                  lisPreguntas.add(prg17);
                }else {
                  Preguntas prg171 = new Preguntas();            
                  prg171.setNumeroP("12");
                  prg171.setPregunta0Rd(salud.get(0).getPregunta17Rd6());
                  prg171.setPregunta0Rd1(salud.get(0).getPregunta17Rd7());
                  prg171.setPregunta0Rd2(salud.get(0).getPregunta17Rd8());
                  prg171.setPregunta0Rd3(salud.get(0).getPregunta17Rd9());
                  prg171.setPregunta0Rd4(salud.get(0).getPregunta17Rd10());
                  lisPreguntas.add(prg171);
                }
      
             }

            return lisPreguntas;
        }

    }