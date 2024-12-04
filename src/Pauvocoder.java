import static java.lang.System.exit;
import java.util.logging.Logger;

public class Pauvocoder {

    private static final Logger LOGGER = Logger.getLogger(Pauvocoder.class.getName());
    private static final String NOT_IMPLEMENTED = "Not implemented yet";

    // Processing SEQUENCE size (100 msec with 44100Hz samplerate)
    static final int SEQUENCE = StdAudio.SAMPLE_RATE/10;

    // Overlapping size (20 msec)
    static final int OVERLAP = SEQUENCE/5 ;
    // Best OVERLAP offset seeking window (15 msec)
    static final int SEEK_WINDOW = 3*OVERLAP/4;

    public static void main(String[] args) {
        if (args.length < 2)
        {
            LOGGER.severe("usage: pauvocoder <input.wav> <freqScale>");
            exit(1);
        }


        String wavInFile = args[0];
        double freqScale = Double.parseDouble(args[1]);
        String outPutFile= wavInFile.split("\\.")[0] + "_" + freqScale +"_";

        // Open input .wev file
        double[] inputWav = StdAudio.read(wavInFile);

        // Resample test
        double[] newPitchWav = resample(inputWav, freqScale);
        StdAudio.save(outPutFile+"Resampled.wav", newPitchWav);

        // Echo test
        double[] echoWav = echo(newPitchWav, 1000, 0.2);
        StdAudio.save(outPutFile+"Echo.wav", echoWav);


        // Simple dilatation
        double[] outputWav   = vocodeSimple(newPitchWav, 1.0/freqScale);
        StdAudio.save(outPutFile+"Simple.wav", outputWav);

    

        // Simple dilatation with overlaping
        outputWav = vocodeSimpleOver(newPitchWav, 1.0/freqScale);
        StdAudio.save(outPutFile+"SimpleOver.wav", outputWav);

        // Simple dilatation with overlaping and maximum cross correlation search
        outputWav = vocodeSimpleOverCross(newPitchWav, 1.0/freqScale);
        StdAudio.save(outPutFile+"SimpleOverCross.wav", outputWav);

        joue(outputWav);
        // Display waveform
        displayWaveform(outputWav);

    }

    /**
     * Resample inputWav with freqScale
     * @param inputWav
     * @param freqScale
     * @return resampled wav
     */
    public static double[] resample(double[] inputWav, double freqScale) {
        int newSize = (int)(inputWav.length / freqScale);
        double[] output = new double[newSize];
        
        if (freqScale > 1) {
            // Sous-échantillonnage
            for (int i = 0; i < newSize; i++) {
                int pos = (int)(i * freqScale);
                if (pos < inputWav.length) {
                    output[i] = inputWav[pos];
                }
            }
        } else {
            // Sur-échantillonnage
            for (int i = 0; i < newSize; i++) {
                int indexInterpolation = (int)(i * freqScale);

                if (i % 2 == 0) {
                    // Copie l'échantillon existant
                    output[i] = inputWav[indexInterpolation];

                    if (indexInterpolation+1 >= inputWav.length) {
                        output[i] = inputWav[indexInterpolation];
                    }

                } else {
                    // Interpolation entre les échantillons adjacents

                    if (indexInterpolation+1 >= inputWav.length) {
                        output[i] = inputWav[indexInterpolation];
                    } else {
                        double pointBase = inputWav[indexInterpolation];
                        double pointAdjacent = inputWav[indexInterpolation + 1];
                        double interpolation = (pointBase + pointAdjacent) / 2.0;
                        output[i] = (int) interpolation;
                    }
                }
            }
        }
        
        return output;
    }

    /**
     * Simple dilatation, without any overlapping
     * @param inputWav
     * @param dilatation factor
     * @return dilated wav
     */
    public static double[] vocodeSimple(double[] inputWav, double dilatation) {
        // Nouvelle liste avec la taille dilatée
        // Calculer le nombre d'échantillons
        int nouvelleListe = (int)(inputWav.length * dilatation);
        double[] output = new double[nouvelleListe];

        int tailleNormale = inputWav.length;
        int tailleDilatation = output.length;
        int tailleSaut = 100; //100 ms
        int tailleSautSamples = tailleSaut * StdAudio.SAMPLE_RATE / 1000;

        for (int i = 0; i < tailleDilatation; i++) {
            output[i] = inputWav[i * tailleSautSample];
        }

        return output;

    }

    /**
     * Simple dilatation, with overlapping
     * @param inputWav
     * @param dilatation factor
     * @return dilated wav
     */
    public static double[] vocodeSimpleOver(double[] inputWav, double dilatation) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Simple dilatation, with overlapping and maximum cross correlation search
     * @param inputWav
     * @param dilatation factor
     * @return dilated wav
     */
    public static double[] vocodeSimpleOverCross(double[] inputWav, double dilatation) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Play the wav
     * @param wav
     */
    public static void joue(double[] wav) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Add an echo to the wav
     * @param wav
     * @param delay in msec
     * @param gain
     * @return wav with echo
     */
    public static double[] echo(double[] wav, int delayMs, double attn) {
        int delaySamples = delayMs * StdAudio.SAMPLE_RATE / 1000;
        double[] echo = new double[wav.length + delaySamples];

        for (int i = 0; i < wav.length; i++) {
        
            echo[i] += wav[i];

            
            if (i + delaySamples < echo.length) {
                echo[i + delaySamples] += wav[i] * attn;
            }
        }

        
        for (int i = 0; i < echo.length; i++) {
            if (echo[i] > 1.0) {
                echo[i] = 1.0;
            } else if (echo[i] < -1.0) {
                echo[i] = -1.0;
            }
        }

        return echo;
    }


    /**
     * Display the waveform
     * @param wav
     */
    public static void displayWaveform(double[] wav) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }


}
